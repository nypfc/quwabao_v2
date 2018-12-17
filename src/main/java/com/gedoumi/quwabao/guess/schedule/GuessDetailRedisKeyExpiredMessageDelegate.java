package com.gedoumi.quwabao.guess.schedule;

import com.gedoumi.common.enums.GuessDetailStatusEnum;
import com.gedoumi.common.enums.GuessNotityTypeEnum;
import com.gedoumi.common.enums.GuessStatusEnum;
import com.gedoumi.guess.entity.Guess;
import com.gedoumi.guess.entity.GuessDetail;
import com.gedoumi.guess.service.GuessBetService;
import com.gedoumi.guess.service.GuessDetailService;
import com.gedoumi.guess.service.GuessService;
import com.gedoumi.guess.vo.RankingVO;
import com.gedoumi.quwabao.common.enums.GuessDetailStatusEnum;
import com.gedoumi.quwabao.common.enums.GuessNotityTypeEnum;
import com.gedoumi.quwabao.common.enums.GuessStatusEnum;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.guess.dataobj.model.Guess;
import com.gedoumi.quwabao.guess.dataobj.model.GuessDetail;
import com.gedoumi.quwabao.guess.service.GuessBetService;
import com.gedoumi.quwabao.guess.service.GuessDetailService;
import com.gedoumi.quwabao.guess.service.GuessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Redis Key过期通知类
 *
 * @author Minced
 */
@Slf4j
public class GuessDetailRedisKeyExpiredMessageDelegate {

    @Resource
    private GuessService guessService;

    @Resource
    private GuessDetailService guessDetailService;

    @Resource
    private GuessBetService guessBetService;

    @Resource
    private RedisCache redisCache;

    /**
     * Redis过期通知（回调）
     *
     * @param message 返回信息（此参数为过期的key值）
     */
    @Transactional(rollbackFor = Exception.class)
    public void guessDetailExpired(String message) {
        // 如果不是竞猜详情，直接返回
        if (!StringUtils.equals(message.split("-")[0], "guessDetail")) {
            return;
        }
        // 获取ID查询竞猜详情
        Long guessDetailId = Long.parseLong(message.split("-")[1]);
        GuessDetail guessDetail = guessDetailService.findById(guessDetailId);
        // 查询竞猜期
        Guess guess = guessService.findById(guessDetail.getGuessId());
        // 判断竞猜详情状态
        Integer guessDetailStatus = guessDetail.getGuessDetailStatus();
        if (guessDetailStatus.equals(GuessDetailStatusEnum.BETTING.getCode())) {
            // 从下注期到开奖期
            log.debug("下注期 -- 游戏期");
            // 获取排名
            String ranking = guessDetailService.getRanking(guessDetail, guess.getGuessReturn());
            RankingVO rankingVO = new RankingVO();
            rankingVO.setIssue(String.valueOf(guessDetail.getIssueNumber()));
            rankingVO.setCar1(String.valueOf(ranking.charAt(0)));
            rankingVO.setCar2(String.valueOf(ranking.charAt(1)));
            rankingVO.setCar3(String.valueOf(ranking.charAt(2)));
            rankingVO.setCar4(String.valueOf(ranking.charAt(3)));
            rankingVO.setCar5(String.valueOf(ranking.charAt(4)));
            rankingVO.setCar6(String.valueOf(ranking.charAt(5)));
            // 给前端发送游戏开始的通知
            guessService.sendMessage(GuessNotityTypeEnum.GAME_START, rankingVO);
            // 重新设置Redis，不需要设置value
            redisCache.setExpireKeyValueData(message, null, guess.getGameTime(), TimeUnit.SECONDS);

        } else if (guessDetailStatus.equals(GuessDetailStatusEnum.GAMING.getCode())) {
            // 从游戏到结果展示期
            log.debug("游戏期 -- 结果展示期");
            guessDetail.setGuessDetailStatus(GuessDetailStatusEnum.SHOW_RESULT.getCode());
            guessDetailService.save(guessDetail);
            // 重新设置Redis，不需要设置value
            redisCache.setExpireKeyValueData(message, null, guess.getBounsTime(), TimeUnit.SECONDS);
            // 修改所有中奖的下注状态和发奖金
            guessBetService.guessRight(guessDetail);

        } else if (guessDetailStatus.equals(GuessDetailStatusEnum.SHOW_RESULT.getCode())) {
            // 从结果展示期到下一个周期
            log.debug("结果展示期 -- 下一个周期");
            guessDetail.setGuessDetailStatus(GuessDetailStatusEnum.FINISHED.getCode());
            guessDetailService.save(guessDetail);
            // 判断当前时间是否大于竞猜期结束的时间，如果大于则直接停止创建后续的竞猜详情
            Date now = new Date();
            if (now.getTime() >= guess.getEndTime().getTime()) {
                // 把竞猜期状态置为结束
                log.debug("已超出竞猜期结束时间，不在创建下一周期");
                guess.setGuessStatus(GuessStatusEnum.FINISHED.getCode());
                guessService.save(guess);
                // 给前端发送投注期结束的通知
                guessService.sendMessage(GuessNotityTypeEnum.GUESS_END, null);
                // 自动创建下一场竞猜期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                Date startTime;
                Date endTime;
                // 判断当前时间
                // 如果是18点以前，创建当天18:00-21:00时间段的竞猜期
                // 如果是18点以后，创建第二天11:00-14:00时间段的竞猜期
                if (calendar.get(Calendar.HOUR_OF_DAY) < 18) {
                    calendar.set(Calendar.HOUR_OF_DAY, 18);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    startTime = calendar.getTime();

                    calendar.set(Calendar.HOUR_OF_DAY, 21);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    endTime = calendar.getTime();
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 11);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    startTime = calendar.getTime();

                    calendar.set(Calendar.HOUR_OF_DAY, 13);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    endTime = calendar.getTime();
                }
                // 判断开始时间与结束时间是否已经有重复的竞猜期，如果有，则不创建
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (guessService.countByTime(sdf.format(startTime)) > 0 || guessService.countByTime(sdf.format(endTime)) > 0) {
                    log.info("已经有重复的竞猜期");
                    return;
                }
                // 创建
                Guess saveGuess = new Guess();
                saveGuess.setStartTime(startTime);
                saveGuess.setEndTime(endTime);
                guessService.save(saveGuess);
                return;
            }
            log.debug("创建下一个竞猜详情");
            // 创建新的竞猜详情、各玩法的赔率与各玩法的投注金额，获取返回的竞猜详情ID
            Long newGuessDetailId = guessDetailService.createGuessDetailAndOddsAndMoney(guess, new Date());
            // 设置Redis，过期时间从Guess对象中获取
            redisCache.setExpireKeyValueData("guessDetail-" + newGuessDetailId, null, guess.getBetTime(), TimeUnit.SECONDS);
            // 给前端发送下注开始的通知
            guessService.sendMessage(GuessNotityTypeEnum.BET_START, null);

        } else {
            log.error("其他情况");
        }
    }

}
