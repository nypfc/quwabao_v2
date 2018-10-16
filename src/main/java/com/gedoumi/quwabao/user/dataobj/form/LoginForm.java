package com.gedoumi.quwabao.user.dataobj.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录表单
 *
 * @author Minced
 */
@Data
public class LoginForm {

    /**
     * 用户名
     */
    @NotBlank
    private String mobile;

    /**
     * 密码
     */
    @NotBlank
    private String password;

}
