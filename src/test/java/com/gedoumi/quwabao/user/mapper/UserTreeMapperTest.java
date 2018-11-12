package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.model.UserTree;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@Component
public class UserTreeMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserTreeMapper userTreeMapper;

    @Test
    public void selectByParentId() {
        List<User> users = userTreeMapper.selectByParentId(174L);
        System.out.println(JsonUtil.objectToJson(users));
    }

}