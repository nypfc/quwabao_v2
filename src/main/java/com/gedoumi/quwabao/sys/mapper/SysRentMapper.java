package com.gedoumi.quwabao.sys.mapper;

import com.gedoumi.quwabao.sys.dataobj.model.SysRent;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 矿机Mapper
 *
 * @author Minecd
 */
@Mapper
public interface SysRentMapper {

    /**
     * 获取激活的矿机列表
     *
     * @param rentStatus 矿机状态
     * @return 矿机集合
     */
    List<SysRent> selectActiveRents(Integer rentStatus);

    /**
     * 根据矿机类型查询矿机列表
     *
     * @param types 矿机类型
     * @return 矿机集合
     */
    List<SysRent> selectInTypes(Collection<Integer> types);

    /**
     * 根据矿机类型查询矿机
     *
     * @param type 矿机类型
     * @return 矿机对象
     */
    SysRent selectByType(Integer type);

    /**
     * 创建矿机
     *
     * @param sysRent 矿机对象
     * @return 数据库受影响行数
     */
    Integer insert(SysRent sysRent);

    /**
     * 根据ID更新矿机
     *
     * @param sysRent 矿机对象
     * @return 数据库受影响行数
     */
    Integer updateById(SysRent sysRent);

}
