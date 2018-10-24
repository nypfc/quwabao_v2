package com.gedoumi.quwabao.miner.mapper;

import com.gedoumi.quwabao.miner.dataobj.model.Rent;
import com.gedoumi.quwabao.miner.dataobj.model.UserRent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 矿机Mapper
 *
 * @author Minecd
 */
@Mapper
public interface MinerMapper {

    /**
     * 查询矿机
     *
     * @param rentStatus 矿机状态
     * @return 矿机集合
     */
    List<Rent> queryList(Integer rentStatus);

    /**
     * 查询用户租用矿机信息
     *
     * @param userId         用户ID
     * @param userRentStatus 租用的矿机的状态
     * @return 矿机信息集合
     */
    List<UserRent> queryUserRent(Long userId, Integer userRentStatus);

}
