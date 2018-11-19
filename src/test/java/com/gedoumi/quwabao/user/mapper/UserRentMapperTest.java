package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.enums.RentStatusEnum;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.sys.dataobj.model.SysRent;
import com.gedoumi.quwabao.sys.service.SysRentService;
import com.gedoumi.quwabao.user.dataobj.dto.UserRentDTO;
import com.gedoumi.quwabao.user.dataobj.model.UserRent;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@Component
public class UserRentMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserRentMapper userRentMapper;

    @Resource
    private SysRentService sysRentService;

    @Test
    public void selectAllActiveRents() {
        List<UserRentDTO> dtos = userRentMapper.selectAllActiveRents(1);
        System.out.println(JsonUtil.objectToJson(dtos));
    }

    @Test
    public void insert() {

        for (long id = 314L; id <= 394L; id++) {
            UserRent userRent = new UserRent();
            Date now = new Date();
            userRent.setUpdateTime(now);
            userRent.setCreateTime(now);
            userRent.setUserId(id);
            SysRent sysRent = sysRentService.getRent(1);
            userRent.setRentType(sysRent.getRentCode());  // 矿机类型
            userRent.setRentAsset(sysRent.getMoney());  // 矿机价格
            userRent.setLastDig(BigDecimal.ZERO);  // 昨日收益
            userRent.setAlreadyDig(BigDecimal.ZERO);  // 初始化已获得收益
            userRent.setTotalAsset(sysRent.getProfitMoneyExt());  // 总收益
            userRent.setRentStatus(RentStatusEnum.ACTIVE.getValue());  // 状态为激活
            userRent.setFirstRentType(0);
            userRent.setDays(0);  // 冗余字段
            userRentMapper.insert(userRent);
        }
    }

}