package com.gedoumi.quwabao.sys.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 国家编码
 *
 * @author Minced
 */
@Alias("SysZone")
@Data
public class SysZone {

    /**
     * ID
     */
    private Long id;

    /**
     * 国家编码
     */
    private String zoneId;

    /**
     * 国家名称
     */
    private String zoneName;

}