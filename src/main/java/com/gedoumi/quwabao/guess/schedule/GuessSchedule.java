package com.gedoumi.quwabao.guess.schedule;

import com.gedoumi.common.enums.GuessNotityTypeEnum;
import com.gedoumi.common.enums.GuessStatusEnum;
import com.gedoumi.guess.entity.Guess;
import com.gedoumi.guess.service.GuessDetailService;
import com.gedoumi.guess.service.GuessService;
import com.gedoumi.quwabao.common.enums.GuessNotityTypeEnum;
import com.gedoumi.quwabao.common.enums.GuessStatusEnum;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.guess.dataobj.model.Guess;
import com.gedoumi.quwabao.guess.service.GuessDetailService;
import com.gedoumi.quwabao.guess.service.GuessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 竞猜定时任务
 * @author Minced
 */
@Slf4j
@Component
public class GuessSchedule {

    @Resource
    private GuessService guessService;

    @Resource
    private GuessDetailService guessDetailService;

    @Resource
    private RedisCache redisCache;

    /**
     * 每分钟执行，查询数据库，判断是否是竞猜期
     */
    @Scheduled(cron = "0 */1 * * * ?", zone = "Asia/Shanghai")
    public void guessSchedule() throws ParseException {
        // 获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDateTime = new Date();
        log.debug("执行了一次，时间：" + sdf.format(startDateTime));
        // 查询竞猜期，未查询到则直接返回
        Guess guess = guessService.findByStartTime(sdf.parse(sdf.format(startDateTime)));
        if (guess == null) return;
        // 修改竞猜状态
        guess.setGuessStatus(GuessStatusEnum.BEGINNING.getValue());
        guessService.save(guess);
        // 创建新的竞猜详情、各玩法的赔率与各玩法的投注金额，获取返回的竞猜详情ID
        Long guessDetailId = guessDetailService.createGuessDetailAndOddsAndMoney(guess, startDateTime);
        // 设置Redis，过期时间从Guess对象中获取
        redisCache.setExpireKeyValueData("guessDetail-" + guessDetailId, null, guess.getBetTime(), TimeUnit.SECONDS);
        // 给前端发送投注开始的通知
        guessService.sendMessage(GuessNotityTypeEnum.BET_START, null);
        log.debug("执行完成");
    }

}
