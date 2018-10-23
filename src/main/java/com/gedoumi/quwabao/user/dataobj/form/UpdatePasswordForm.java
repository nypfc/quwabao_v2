package com.gedoumi.quwabao.user.dataobj.form;

import lombok.Data;

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
    private String orgPswd;

    /**
     * 新密码
     */
    private String pswd;

}
