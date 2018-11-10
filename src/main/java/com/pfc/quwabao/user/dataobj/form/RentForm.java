package com.pfc.quwabao.user.dataobj.form;

import com.pfc.quwabao.common.validate.IntegerValue;
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
    @NotNull
    @IntegerValue({0, 1, 2, 3, 4, 5})
    private Integer rentType;

    /**
     * 用户密码
     */
    @NotBlank
    private String password;

}
