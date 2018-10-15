package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.user.mapper.UserCheckMapper;
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
    private UserCheckMapper userCheckMapper;

    /**
     * 查询手机号是否被使用
     *
     * @param mobile 手机号
     * @return Boolean
     */
    public Boolean checkMobilePhone(String mobile) {
        return userCheckMapper.countByMobilePhone(mobile) != 0;
    }

    /**
     * 验证邀请码的有效性
     *
     * @param regInviteCode 邀请码
     * @return Boolean
     */
    public Boolean checkInviteCode(String regInviteCode) {
        return userCheckMapper.countByInviteCode(regInviteCode) != 0;
    }

    /**
     * 验证用户名的有效性
     *
     * @param username 用户名
     * @return Boolean
     */
    public Boolean checkUsername(String username) {
        return userCheckMapper.countByUsername(username) != 0;
    }

}
