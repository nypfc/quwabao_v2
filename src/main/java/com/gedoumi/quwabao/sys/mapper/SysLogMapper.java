package com.gedoumi.quwabao.sys.mapper;

import com.gedoumi.quwabao.sys.dataobj.model.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网关日志Mapper
 *
 * @author Minced
 */
@Mapper
public interface SysLogMapper {

    /**
     * 创建网关日志
     *
     * @param sysLog 日志对象
     */
    void createSysLog(SysLog sysLog);

}
