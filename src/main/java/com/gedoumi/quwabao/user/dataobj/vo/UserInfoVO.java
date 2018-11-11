package com.gedoumi.quwabao.user.dataobj.vo;

import lombok.Data;

/**
 * 用户信息VO
 *
 * @author Minced
 */
@Data
public class UserInfoVO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 邀请码
     */
    private String inviteCode;

}
