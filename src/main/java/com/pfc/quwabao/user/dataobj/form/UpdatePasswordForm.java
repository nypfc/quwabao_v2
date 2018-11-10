package com.pfc.quwabao.user.dataobj.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码表单
 *
 * @author Minced
 */
@Data
public class UpdatePasswordForm {

    /**
     * 原密码
     */
    @NotBlank
    private String originalPassword;

    /**
     * 新密码
     */
    @NotBlank
    private String password;

}
