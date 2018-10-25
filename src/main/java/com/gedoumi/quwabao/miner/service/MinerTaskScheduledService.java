package com.gedoumi.quwabao.miner.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 挖矿定时任务Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class MinerTaskScheduledService {

    /**
     * 计算挖矿收益
     */
    @Scheduled(cron = "0 30 23 * * ? ", zone = "Asia/Shanghai")
    public void runDig() {
        System.out.println(123);
        log.info(" ==============开始统计挖矿收益===========");
//        assetService.digJob();
        log.info(" ==============统计挖矿收益结束===========");
    }

    /**
     * 计算推荐人奖励
     */
    @Scheduled(cron = "0 0 23 * * ? ", zone = "Asia/Shanghai")
    public void runReward() {
        log.info(" ==============开始处理推荐人奖励===========");
//        assetService.rewardTask();
        log.info(" ==============处理推荐人奖励结束===========");
    }

}
