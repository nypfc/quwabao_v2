package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.user.dataobj.model.UserTeamRent;
import com.gedoumi.quwabao.user.mapper.UserTeamRentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户团队业绩记录Service
 *
 * @author Minced
 */
@Service
public class UserTeamRentService {

    @Resource
    private UserTeamRentMapper userTeamRentMapper;

    /**
     * 添加用户团队业绩记录
     *
     * @param userTeamRent 记录对象
     */
    public void insert(UserTeamRent userTeamRent) {
        userTeamRentMapper.insert(userTeamRent);
    }

}
