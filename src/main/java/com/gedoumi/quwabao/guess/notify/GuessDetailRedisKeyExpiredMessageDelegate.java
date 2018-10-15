package com.gedoumi.quwabao.guess.notify;

import com.gedoumi.quwabao.common.enums.GuessDetailStatusEnum;
import com.gedoumi.quwabao.common.enums.GuessNotityTypeEnum;
import com.gedoumi.quwabao.common.enums.GuessStatusEnum;
import com.gedoumi.quwabao.guess.entity.Guess;
import com.gedoumi.quwabao.guess.entity.GuessDetail;
import com.gedoumi.quwabao.guess.service.GuessBetService;
import com.gedoumi.quwabao.guess.service.GuessDetailService;
import com.gedoumi.quwabao.guess.service.GuessService;
import com.gedoumi.quwabao.guess.vo.RankingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Redis Key过期通知类
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
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis过期通知（回调）
     * @param message 返回信息（此参数为过期的key值）
     */
    @Transactional
    public void guessDetailExpired(String message) {
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
            // 获取矿车排名
            String ranking = guessDetailService.getRanking(guessDetail, guess.getGuessReturn());
            RankingVO rankingVO = new RankingVO();
            rankingVO.setR1(String.valueOf(ranking.charAt(0)));
            rankingVO.setR2(String.valueOf(ranking.charAt(1)));
            rankingVO.setR3(String.valueOf(ranking.charAt(2)));
            rankingVO.setR4(String.valueOf(ranking.charAt(3)));
            rankingVO.setR5(String.valueOf(ranking.charAt(4)));
            rankingVO.setR6(String.valueOf(ranking.charAt(5)));
            // 给前端发送游戏开始的通知
            guessService.sendMessage(GuessNotityTypeEnum.GAME_START, rankingVO);
            // 重新设置Redis，不需要设置value
            redisTemplate.opsForValue().set(message, null, guess.getGameTime(), TimeUnit.SECONDS);

        } else if (guessDetailStatus.equals(GuessDetailStatusEnum.GAMING.getCode())) {
            // 从游戏到算奖期
            log.debug("游戏期 -- 算奖期");
            guessDetail.setGuessDetailStatus(GuessDetailStatusEnum.BOUNS.getCode());
            guessDetailService.save(guessDetail);
            // 重新设置Redis，不需要设置value
            redisTemplate.opsForValue().set(message, null, guess.getBounsTime(), TimeUnit.SECONDS);
            // 修改所有中奖的下注状态和发奖金
            guessBetService.guessRight(guessDetail);

        } else if (guessDetailStatus.equals(GuessDetailStatusEnum.BOUNS.getCode())) {
            // 从游戏期到下一个周期
            log.debug("算奖期 -- 下一个周期");
            guessDetail.setGuessDetailStatus(GuessDetailStatusEnum.FINISHED.getCode());
            guessDetailService.save(guessDetail);
            // 判断当前时间是否大于竞猜期结束的时间，如果大于则直接停止创建后续的竞猜详情
            if (new Date().getTime() >= guess.getEndTime().getTime()) {
                // 把竞猜期状态置为结束
                log.debug("已超出竞猜期结束时间，不在创建下一周期");
                guess.setGuessStatus(GuessStatusEnum.FINISHED.getCode());
                guessService.save(guess);
                // 给前端发送投注期结束的通知
                guessService.sendMessage(GuessNotityTypeEnum.GUESS_END, null);
                return;
            }
            log.debug("创建下一个竞猜详情");
            // 创建新的竞猜详情、各玩法的赔率与各玩法的投注金额，获取返回的竞猜详情ID
            Long newGuessDetailId = guessDetailService.createGuessDetailAndOddsAndMoney(guess, new Date());
            // 设置Redis，过期时间从Guess对象中获取
            redisTemplate.opsForValue().set("guessDetail-" + newGuessDetailId, null, guess.getBetTime(), TimeUnit.SECONDS);
            // 给前端发送下注开始的通知
            guessService.sendMessage(GuessNotityTypeEnum.BET_START, null);

        } else {
            log.error("其他情况");
        }
    }

}
