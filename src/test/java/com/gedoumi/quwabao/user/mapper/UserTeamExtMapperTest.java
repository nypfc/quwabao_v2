package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.enums.TeamLevelEnum;
import com.gedoumi.quwabao.common.enums.TeamLevelManualEnum;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.user.dataobj.model.UserTeamExt;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class UserTeamExtMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserTeamExtMapper userTeamExtMapper;

    @Test
    public void insert() {
        Long userId = 312L;

        Date now = new Date();
        UserTeamExt userTeamExt = new UserTeamExt();
        userTeamExt.setUserId(userId);
        userTeamExt.setTeamLevel(TeamLevelEnum.LEVEL_0.getValue());
        userTeamExt.setManualTeamLevel(TeamLevelManualEnum.AUTO.getValue());
        userTeamExt.setTeamTotalStaticProfit(BigDecimal.ZERO);
        userTeamExt.setTeamTotalRent(BigDecimal.ZERO);
        userTeamExt.setCreateTime(now);
        userTeamExt.setUpdateTime(now);
        userTeamExtMapper.insert(userTeamExt);

        System.out.println(JsonUtil.objectToJson(userTeamExt));

        UserTeamExt userTeamExt1 = new UserTeamExt();
        userTeamExt1.setUserId(userId);
        userTeamExt1.setTeamTotalRent(new BigDecimal("2.30000"));
        userTeamExt1.setTeamLevel(4);
        userTeamExtMapper.updateByUserId(userTeamExt1);
    }

}