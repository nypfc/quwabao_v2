package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.user.dataobj.model.UserRent;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class UserRentServiceTest extends QuwabaoApplicationTests {

    @Resource
    private UserRentService userRentService;

    @Test
    public void getAllUserActiveRent() {
        List<UserRent> rents = userRentService.getAllUserActiveRent();
        System.out.println(JsonUtil.objectToJson(rents));
    }

}