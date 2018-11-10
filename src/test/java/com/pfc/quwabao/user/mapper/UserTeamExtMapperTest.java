package com.pfc.quwabao.user.mapper;

import com.pfc.quwabao.QuwabaoApplicationTests;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserTeamExtMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserTeamExtMapper userTeamExtMapper;

    @Test
    public void queryUserIdsByParentId() {
        List<Long> userIds = userTeamExtMapper.queryUserIdsByParentId(Lists.newArrayList(138L));
        System.out.println(userIds);
    }

}