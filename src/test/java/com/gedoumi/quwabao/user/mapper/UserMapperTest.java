package com.gedoumi.quwabao.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class UserMapperTest extends QuwabaoApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    public void test() {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getMobilePhone, "13810060370"));
        System.out.println(JsonUtil.objectToJson(user));
    }

    @Test
    public void test2() {
        Integer count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getMobilePhone, "13810060371"));
        System.out.println(count);
    }

    @Test
    public void test3() {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getMobilePhone, "13810060370"));
        int update = userMapper.update(user, new UpdateWrapper<User>().lambda()
                .set(User::getErrorCount, 0)
                .set(User::getErrorTime, new Date()));
        System.out.println(update);
    }

}