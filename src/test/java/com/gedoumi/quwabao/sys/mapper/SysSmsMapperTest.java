package com.gedoumi.quwabao.sys.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.utils.CurrentDateUtil;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@Component
public class SysSmsMapperTest extends QuwabaoApplicationTests {

    @Resource
    private SysSmsMapper sysSmsMapper;

    @Test
    public void createSysSms() {
    }

    @Test
    public void smsCurrentDayCount() {
        CurrentDateUtil date = new CurrentDateUtil();
        List<Date> dateList = sysSmsMapper.smsCurrentDayCount("13718156453", date.getStartTime(), date.getEndTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> collect = dateList.stream().map(sdf::format).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void checkSms() {
    }

    @Test
    public void updateSmsStatus() {
    }

}