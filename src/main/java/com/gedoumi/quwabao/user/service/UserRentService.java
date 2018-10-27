package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.UserRentStatus;
import com.gedoumi.quwabao.user.dataobj.model.UserRent;
import com.gedoumi.quwabao.user.mapper.UserRentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户矿机Service
 *
 * @author Minced
 */
@Service
public class UserRentService {

    @Resource
    private UserRentMapper userRentMapper;

    /**
     * 获取用户租用矿机信息列表
     *
     * @param userId 用户ID
     * @return 矿机信息集合
     */
    public List<UserRent> getUserRent(Long userId) {
        return userRentMapper.queryUserRent(userId, UserRentStatus.Active.getValue());
    }

}
