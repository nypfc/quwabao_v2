package com.gedoumi.quwabao.schedule;

import com.gedoumi.quwabao.user.service.UserAssetDetailService;
import com.gedoumi.quwabao.user.service.UserAssetService;
import com.gedoumi.quwabao.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 定时任务执行
 *
 * @author Minced
 */
@Service
public class RunScheduleTask {

    @Resource
    private UserService userService;

    @Resource
    private UserAssetService userAssetService;

    @Resource
    private UserAssetDetailService userAssetDetailService;

    /**
     * 计算挖矿收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void digTask() {

    }

    /**
     * 计算推荐人收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void rewardTask() {

    }

    /**
     * 计算俱乐部收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void clubRewardTask() {

    }

}
