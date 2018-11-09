package com.gedoumi.quwabao.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.enums.UserStatus;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class UserMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    public void userTest() {
        User queryResult = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getMobilePhone, "13810060371"));
        User user = Optional.ofNullable(queryResult).filter(u -> {
            boolean b = u.getMobilePhone() != null;
            if (!b)
                System.out.println("emmmmm");
            return b;
        }).filter(u -> {
            boolean b = u.getUserStatus() != UserStatus.Disable.getValue();
            if (!b)
                System.out.println(123);
            return b;
        }).orElseThrow(RuntimeException::new);
        System.out.println(user);
    }

}