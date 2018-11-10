package com.pfc.quwabao.sys.dataobj.model;


import lombok.Data;

import java.util.Date;

/**
 * 网关充值记录
 */
@Data
public class SysLog {

    private Long id;

    /**
     * 状态
     * 1：可用
     * 0：不可用
     */
    private Integer logStatus;

    private String requestUrl;

    private String requestBody;

    private String clientIp;

    private String seq;

    private Date createTime;

    private Date updateTime;

    private String mobile;

    private Integer logType;

}
