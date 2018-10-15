package com.gedoumi.quwabao.task;

import com.gedoumi.quwabao.asset.service.UserAssetService;
import com.gedoumi.quwabao.common.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Component
public class DigTask {
    Logger logger = LoggerFactory.getLogger(DigTask.class);

    public static  final BigDecimal UN_FROZEN_RATE = new BigDecimal("0.005");

    @Resource
    private UserAssetService assetService;

    @Resource
    private AppConfig appConfig;

    private Date date;

    public DigTask() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 推荐人奖励
     * 30天发放
     */
//    @Scheduled(cron = "0/30 * * * * ? ", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 0 23 * * ? ", zone = "Asia/Shanghai")
    public void runReward(){
        if(!appConfig.isOnTrans()){
            logger.info("还未上线交易所，不处理推荐人奖励");
            return;
        }
        logger.info(" ==============开始处理推荐人奖励===========");
        if(date == null){
            date = new Date();
        }
        assetService.rewardTask(date);
        date = null;
        logger.info(" ==============处理推荐人奖励结束===========");
    }


    /**
     * 解冻资产（天使币）
     * 每日0.5%的比例解冻
     */
//    @Scheduled(cron = "0/30 * * * * ? ", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 20 23 * * ? ", zone = "Asia/Shanghai")
    public void runUnFrozen(){
        if(!appConfig.isOnTrans()){
            logger.info("还未上线交易所，不处理解冻");
            return;
        }
        logger.info(" ==============开始解冻天使币===========");
        if(date == null){
            date = new Date();
        }
        assetService.unFrozenUser(date, UN_FROZEN_RATE);
        date = null;
        logger.info(" ==============解冻天使币结束===========");
    }

//    @Scheduled(cron = "0/30 * * * * ? ", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 30 23 * * ? ", zone = "Asia/Shanghai")
    /**
     * 挖矿收益
     */
    public void runDig(){
        logger.info(" ==============开始统计挖矿收益===========");
        if(date == null){
            date = new Date();
        }
        assetService.digJob(date);
        date = null;
        logger.info(" ==============统计挖矿收益结束===========");
    }



    /**
     * 解冻团队奖励
     * 90天解冻
     */
//    @Scheduled(cron = "0/30 * * * * ? ", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 40 23 * * ? ", zone = "Asia/Shanghai")
    public void runUnFrozenReward(){
        if(!appConfig.isOnTrans()){
            logger.info("还未上线交易所，不处理团队奖励解冻");
            return;
        }
        logger.info(" ==============开始解冻团队奖励===========");
        if(date == null){
            date = new Date();
        }
        assetService.unFrozenReward(date);
        date = null;
        logger.info(" ==============解冻团队奖励结束===========");
    }



}
