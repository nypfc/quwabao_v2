package com.gedoumi.quwabao.user.dataobj.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户Model
 *
 * @author Minced
 */
@TableName("user")
@Data
public class User {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 登录错误次数
     */
    private Integer errorCount;

    /**
     * 错误时间
     */
    private Date errorTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 令牌
     */
    private String token;

    /**
     * 设备ID
     */
    @TableField("deviceId")
    private String deviceId;

    /**
     * 邀请人的邀请码
     */
    private String regInviteCode;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 性别
     */
    private Integer sex;

    /**
     *
     */
    private String ethAddress;

    /**
     * 实名验证状态
     */
    private Integer validateStatus;

    /**
     * 身份证号
     */
    private String idCard;

}
