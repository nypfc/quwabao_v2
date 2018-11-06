package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserTeamMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserTeamMapper userTeamMapper;

    @Test
    public void queryUserIdsByParentId() {
        List<Long> userIds = userTeamMapper.queryUserIdsByParentId(Lists.newArrayList(138L));
        System.out.println(userIds);
    }

}