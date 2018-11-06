package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Component
public class LoginMapperTest extends QuwabaoApplicationTests {

    @Resource
    private LoginMapper loginMapper;

    @Test
    public void queryByMobilePhone() {
        User user = loginMapper.queryByMobilePhone("13810060370");
        System.out.println(user);
    }

}