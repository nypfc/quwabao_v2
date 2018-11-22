package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.enums.TransTypeEnum;
import com.gedoumi.quwabao.common.utils.CurrentDateUtil;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@Component
public class UserAssetDetailMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserAssetDetailMapper userAssetDetailMapper;

    @Test
    public void selectTotalDayWithdraw() {
        CurrentDateUtil currentDate = new CurrentDateUtil();
        BigDecimal bigDecimal = userAssetDetailMapper.selectTotalDayWithdraw(312L, TransTypeEnum.NetOut.getValue(),
                currentDate.getStartTime(), currentDate.getEndTime());
        System.out.println(bigDecimal);
    }

}