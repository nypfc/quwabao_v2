package com.gedoumi.quwabao.component;

import com.gedoumi.quwabao.rent.service.RentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 定时任务
 *
 * @author Minced
 */
@Slf4j
@Component
@Service
public class ScheduleTask {

    @Resource
    private RentService rentService;

    /**
     * 计算挖矿收益
     */
    @Scheduled(cron = "0 0 23 * * ? ", zone = "Asia/Shanghai")
    public void runDig() {
        log.info(" ==============开始统计挖矿收益===========");
        rentService.digJob();
        log.info(" ==============统计挖矿收益结束===========");
    }

    /**
     * 计算推荐人奖励
     */
    @Scheduled(cron = "0 15 23 * * ? ", zone = "Asia/Shanghai")
    public void runReward() {
        log.info(" ==============开始处理推荐人奖励===========");
        rentService.rewardTask();
        log.info(" ==============处理推荐人奖励结束===========");
    }

}
