package com.pfc.quwabao.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pfc.quwabao.user.dataobj.model.User;
import com.pfc.quwabao.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户验证Service
 *
 * @author Minced
 */
@Service
public class UserCheckService {

    @Resource
    private UserMapper userMapper;

    /**
     * 查询手机号是存在
     *
     * @param mobile 手机号
     * @return Boolean
     */
    public Boolean checkMobilePhone(String mobile) {
        return userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getMobilePhone, mobile)) != 0;
    }

    /**
     * 查询邀请码对应用户是否存在
     *
     * @param inviteCode 邀请码
     * @return Boolean
     */
    public Boolean checkInviteCode(String inviteCode) {
        return userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getInviteCode, inviteCode)) != 0;
    }

    /**
     * 查询用户名对应用户是否存在
     *
     * @param username 用户名
     * @return Boolean
     */
    public Boolean checkUsername(String username) {
        return userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, username)) != 0;
    }

}
