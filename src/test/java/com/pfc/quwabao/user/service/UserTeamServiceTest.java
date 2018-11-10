package com.pfc.quwabao.user.service;

import com.pfc.quwabao.QuwabaoApplicationTests;
import com.pfc.quwabao.common.utils.JsonUtil;
import com.pfc.quwabao.user.dataobj.model.UserTeamExt;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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