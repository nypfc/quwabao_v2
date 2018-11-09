package com.gedoumi.quwabao.sys.dataobj.model;

import com.gedoumi.quwabao.common.enums.SmsStatus;
import lombok.Data;

import java.util.Date;

/**
 * 系统短信
 *
 * @author Minced
 */
@Data
public class SysSms {

    /**
     * ID
     */
    private Long id;

    /**
     * 状态
     * 1：可用
     * 0：不可用
     */
    private Integer smsStatus;

    /**
     * 类型
     * 1：登录
     * 0：注册
     */
    private Integer smsType;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    private Date updateTime = new Date();

    /**
     * 验证码
     */
    private String code;

}
