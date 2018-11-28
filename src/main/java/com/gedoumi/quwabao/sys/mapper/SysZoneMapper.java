package com.gedoumi.quwabao.sys.mapper;

import com.gedoumi.quwabao.sys.dataobj.model.SysZone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 国家编码Mapper
 *
 * @author Minced
 */
@Mapper
public interface SysZoneMapper {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 国家编码对象
     */
    SysZone selectById(Long id);

    /**
     * 查询所有国家编码
     *
     * @return 国家编码集合
     */
    List<SysZone> selectAll();

}