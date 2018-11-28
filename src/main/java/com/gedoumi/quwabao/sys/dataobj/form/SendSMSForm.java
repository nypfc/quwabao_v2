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
     * 国家编码
     */
    @NotBlank
    private String zone;

    /**
     * 手机号
     */
    @NotBlank
    @MobilePhone
    private String mobile;

}
