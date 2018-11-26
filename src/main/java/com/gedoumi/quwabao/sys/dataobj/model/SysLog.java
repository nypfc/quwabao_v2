package com.gedoumi.quwabao.sys.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 网关充值记录
 */
@Alias("SysLog")
@Data
public class SysLog {

    /**
     * ID
     */
    private Long id;

    /**
     * 状态
     * 1：可用
     * 0：不可用
     */
    private Integer logStatus;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求体（请求Json字符串）
     */
    private String requestBody;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 唯一标识
     */
    private String seq;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 日志类型
     */
    private Integer logType;

}
