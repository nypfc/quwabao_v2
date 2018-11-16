package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.sys.dataobj.model.SysConfig;
import com.gedoumi.quwabao.sys.mapper.SysConfigMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 系统配置Service
 *
 * @author Minced
 */
@Service
public class SysConfigService {

    @Resource
    private SysConfigMapper sysConfigMapper;

    /**
     * 获取系统配置
     *
     * @return 系统配置对象
     */
    public SysConfig getSysConfig() {
        return sysConfigMapper.selectById(1L);
    }

}
