package com.gedoumi.quwabao.sys.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.utils.CurrentDateUtil;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SysSmsMapperTest extends QuwabaoApplicationTests {

    @Resource
    private SysSmsMapper sysSmsMapper;

    @Test
    public void smsCurrentDayCount() {
        CurrentDateUtil date = new CurrentDateUtil();
        Integer count = sysSmsMapper.smsCurrentDayCount("13810060371", date.getStartTime(), date.getEndTime());
        System.out.println(count);
    }

    @Test
    public void updateSmsStatus() {
    }

}