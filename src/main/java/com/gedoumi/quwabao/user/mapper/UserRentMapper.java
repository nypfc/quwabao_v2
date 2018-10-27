package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.model.UserRent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户矿机Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserRentMapper {

    /**
     * 查询用户租用矿机信息
     *
     * @param userId         用户ID
     * @param userRentStatus 租用的矿机的状态
     * @return 矿机信息集合
     */
    List<UserRent> queryUserRent(Long userId, Integer userRentStatus);

}
