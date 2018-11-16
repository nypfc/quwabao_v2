package com.gedoumi.quwabao.schedule;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RunScheduleTaskTest extends QuwabaoApplicationTests {

    @Resource
    private RunScheduleTask runScheduleTask;

    @Test
    public void digTask() {
        runScheduleTask.digTask();
    }

    @Test
    public void rewardTask() {
    }

    @Test
    public void clubRewardTask() {
    }

}