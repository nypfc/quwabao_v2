package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.sys.dataobj.model.SysRent;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

@Component
public class SysRentServiceTest extends QuwabaoApplicationTests {

    @Resource
    private SysRentService sysRentService;

    @Test
    public void getRentsInType() {
        HashSet<Integer> set = Sets.newHashSet(0, 1, 1, 3);
        List<SysRent> rents = sysRentService.getRentsInType(set);
        System.out.println(JsonUtil.objectToJson(rents));
    }

}