package com.gedoumi.quwabao.user.dataobj.form;

import com.gedoumi.quwabao.common.validate.MobilePhone;
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
    @MobilePhone
    private String mobile;

    /**
     * 密码
     */
    @NotBlank
    private String password;

}
