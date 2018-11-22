package com.gedoumi.quwabao.trans.dataobj.form;

import lombok.Data;

/**
 * 提现表单
 *
 * @author Minced
 */
@Data
public class WithdrawForm {

    /**
     * 密码
     */
    private String password;

    /**
     * 提现金额
     */
    private String amount;

    /**
     * 以太坊地址
     */
    private String ethAddress;

}
