package com.gedoumi.quwabao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface UserRentMapper extends BaseMapper<UserRent> {

    /**
     * 查询用户租用矿机信息
     *
     * @param userId         用户ID
     * @param userRentStatus 租用的矿机的状态
     * @return 矿机信息集合
     */
    List<UserRent> selectUserRents(Long userId, Integer userRentStatus);

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
