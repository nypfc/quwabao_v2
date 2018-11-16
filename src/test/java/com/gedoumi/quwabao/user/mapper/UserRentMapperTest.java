package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.user.dataobj.dto.UserRentDTO;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@Component
public class UserRentMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserRentMapper userRentMapper;

    @Test
    public void selectAllActiveRents() {
        List<UserRentDTO> dtos = userRentMapper.selectAllActiveRents(1);
        System.out.println(JsonUtil.objectToJson(dtos));
    }

}