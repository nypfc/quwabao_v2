package com.gedoumi.quwabao.user.dataobj.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改用户名表单
 *
 * @author Minced
 */
@Data
public class ResetForm {

    /**
     * 用户名
     */
    @NotBlank
    private String userName;

}
