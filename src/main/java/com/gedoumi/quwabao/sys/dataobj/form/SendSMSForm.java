package com.gedoumi.quwabao.sys.dataobj.form;

import com.gedoumi.quwabao.common.validate.StringValue;
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
    private String mobile;

    /**
     * 短信类型
     */
    @NotBlank
    @StringValue({"0", "1", "2", "3", "4"})
    private String type;

}
