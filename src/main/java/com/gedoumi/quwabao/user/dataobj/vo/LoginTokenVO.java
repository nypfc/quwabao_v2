package com.gedoumi.quwabao.user.dataobj.vo;

import lombok.Data;

/**
 * 登录VO
 *
 * @author Minced
 */
@Data
public class LoginTokenVO {

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 令牌
     */
    private String token;

}
