package com.pfc.quwabao.user.mapper;

import com.pfc.quwabao.user.dataobj.dto.UserRentNumberDTO;
import com.pfc.quwabao.user.dataobj.model.UserRent;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
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

    /**
     * 创建用户租用的矿机
     *
     * @param userRent 用户矿机对象
     */
    void createUserRent(UserRent userRent);

    /**
     * 获取指定用户租用矿机的数量
     *
     * @param userIds        用户ID集合
     * @param userRentStatus 租用的矿机的状态
     * @return 矿机租用数量DTO
     */
    List<UserRentNumberDTO> countUserRentsByIds(List<Long> userIds, Integer userRentStatus);

    /**
     * 查询指定用户的租用矿机价格的总和
     *
     * @param userIds 用户ID集合
     * @return 矿机价格总和
     */
    BigDecimal queryTotalRentAsset(List<Long> userIds);

}
