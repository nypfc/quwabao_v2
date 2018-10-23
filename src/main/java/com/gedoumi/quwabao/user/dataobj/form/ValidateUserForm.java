package com.gedoumi.quwabao.user.dataobj.form;

import lombok.Data;

/**
 * 验证用户表单
 *
 * @author Minced
 */
@Data
public class ValidateUserForm {

    private String realName;

    private String idCard;

    private String base64Img;

    private Long userId;

}
