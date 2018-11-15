package com.gedoumi.quwabao.schedule;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import static org.junit.Assert.*;

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