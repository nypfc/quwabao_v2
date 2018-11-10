package com.pfc.quwabao.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pfc.quwabao.QuwabaoApplicationTests;
import com.pfc.quwabao.user.dataobj.model.User;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    public void userTest() {
        User queryResult = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getToken, "89cbdfac-a0cb-41a7-986a-440288ef377b"));
        System.out.println(queryResult);
    }

}