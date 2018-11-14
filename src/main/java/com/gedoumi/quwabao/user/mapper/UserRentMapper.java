package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.user.dataobj.dto.UserRentNumberDTO;
import com.gedoumi.quwabao.user.dataobj.model.UserRent;
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
     * 根据用户ID查询用户矿机列表
     *
     * @param userId     用户ID
     * @param rentStatus 矿机状态
     * @return 用户矿机集合
     */
    List<UserRent> selectByUserId(Long userId, Integer rentStatus);

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
    BigDecimal selectTotalRentAsset(List<Long> userIds);

    /**
     * 查询所有激活的矿机
     *
     * @param rentStatus 用户矿机状态
     * @return 用户矿机集合
     */
    List<UserRent> selectAllActiveRents(Integer rentStatus);

    /**
     * 创建用户矿机
     *
     * @param userRent 用户矿机对象
     * @return 数据库受影响行数
     */
    Integer insertSelective(UserRent userRent);

    /**
     * 根据ID更新用户矿机
     *
     * @param userRent 用户矿机对象
     * @return 数据库受影响行数
     */
    Integer updateByPrimaryKeySelective(UserRent userRent);

}
