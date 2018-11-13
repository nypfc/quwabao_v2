package com.gedoumi.quwabao.user.dataobj.form;

import com.gedoumi.quwabao.common.validate.StringValue;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 租矿机表单
 *
 * @author Minced
 */
@Data
public class RentForm {

    /**
     * 矿机类型
     */
    @NotBlank
    @StringValue({"0", "1", "2", "3", "4", "5"})
    private String rentType;

    /**
     * 用户密码
     */
    @NotBlank
    private String password;

}
