package com.gedoumi.quwabao.user.dataobj.form;

import com.gedoumi.quwabao.common.validate.StringValue;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新手机号前验证手机号表单
 *
 * @author Minced
 */
@Data
public class CheckBeforUpdateForm {

    /**
     * 支付密码
     */
    private String password;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 验证类型
     * 1:支付密码验证
     * 2:短信验证
     */
    @NotBlank
    @StringValue({"1", "2"})
    private String type;

}
