package com.gedoumi.quwabao.guess.schedule;

import com.gedoumi.quwabao.guess.service.GuessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 竞猜定时任务
 *
 * @author Minced
 */
@Slf4j
@Component
public class GuessSchedule {

    @Resource
    private GuessService guessService;

    /**
     * 每分钟轮询数据库，判断是否是竞猜期
     */
    @Scheduled(cron = "0 */1 * * * ?", zone = "Asia/Shanghai")
    public void guessSchedule() {
        log.info(" ==============竞猜轮询执行一次==============");
//        guessService.schedule();
        log.info(" ==============竞猜轮询执行完成==============");
    }

}
