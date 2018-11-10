package com.pfc.quwabao.user.service;

import com.pfc.quwabao.QuwabaoApplicationTests;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class UserRentServiceTest extends QuwabaoApplicationTests {

    @Resource
    private UserRentService userRentService;

    @Test
    public void getTotalRentAsset() {
        BigDecimal totalRentAsset = userRentService.getTotalRentAsset(Lists.newArrayList(138L));
        System.out.println(Optional.ofNullable(totalRentAsset).orElse(BigDecimal.ZERO));
    }

}