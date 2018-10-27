package com.gedoumi.quwabao.team.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.user.mapper.UserTeamMapper;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;

@Component
public class UserTeamMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserTeamMapper userTeamMapper;

    @Test
    public void queryChildIdListByParentId() {
        List<Long> longList = userTeamMapper.queryChildIdListByParentId(102L);
        System.out.println(longList);
    }

}