package com.gedoumi.quwabao.sys.mapper;

import com.gedoumi.quwabao.sys.dataobj.model.SysConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置Mapper
 *
 * @author Minced
 */
@Mapper
public interface SysConfigMapper {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 系统配置对象
     */
    SysConfig selectById(Long id);

}