package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.user.dataobj.model.UserTeamExt;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@Component
public class UserTeamServiceTest extends QuwabaoApplicationTests {

    @Resource
    private UserTeamService userTeamService;

    @Test
    public void getTeamTotalRentMoney() {
        UserTeamExt userTeamExt = userTeamService.getTeamTotalRentMoney(174L);
        System.out.println(JsonUtil.objectToJson(userTeamExt));
    }

}