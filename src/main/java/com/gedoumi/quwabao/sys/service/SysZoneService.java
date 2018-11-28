package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.sys.dataobj.model.SysZone;
import com.gedoumi.quwabao.sys.mapper.SysZoneMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 国家编码Service
 *
 * @author Minced
 */
@Service
public class SysZoneService {

    @Resource
    private SysZoneMapper sysZoneMapper;

    /**
     * 获取所有国家编码
     *
     * @return 国家编码集合
     */
    public List<SysZone> getAllSysZone() {
        return sysZoneMapper.selectAll();
    }

}
