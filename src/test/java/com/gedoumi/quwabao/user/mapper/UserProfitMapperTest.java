package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.enums.RentStatusEnum;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.user.dataobj.dto.UserProfitDTO;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Component
public class UserProfitMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserProfitMapper userProfitMapper;

    @Test
    public void selectByuserIdAndDate() {
        UserProfitDTO dto = userProfitMapper.selectByuserIdAndDate(312L, "2018-11-19", RentStatusEnum.ACTIVE.getValue());
        System.out.println(JsonUtil.objectToJson(dto));
    }

}