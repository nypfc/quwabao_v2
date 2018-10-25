package com.gedoumi.quwabao.sys.dataobj.form;

import com.gedoumi.quwabao.common.validate.MobilePhone;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 发送短信表单
 *
 * @author Minced
 */
@Data
public class SendSMSForm {

    /**
     * 手机号
     */
    @NotBlank
    @MobilePhone
    private String mobile;

}
