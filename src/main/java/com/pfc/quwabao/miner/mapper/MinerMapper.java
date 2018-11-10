package com.pfc.quwabao.miner.mapper;

import com.pfc.quwabao.miner.dataobj.model.Rent;
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
     * 查询矿机
     *
     * @param rentType 矿机类型
     * @return 矿机对象
     */
    Rent queryRent(Integer rentType);

}
