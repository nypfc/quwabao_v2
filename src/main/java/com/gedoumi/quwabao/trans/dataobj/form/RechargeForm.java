package com.gedoumi.quwabao.trans.dataobj.form;

import lombok.Data;

/**
 * 充值回调表单
 */
@Data
public class RechargeForm {

    /**
     * PFC账号
     */
    private String pfc_account;

    /**
     * 资产名称
     */
    private String asset_name;

    /**
     * 充值金额
     */
    private String amount;

    /**
     * 时间戳
     */
    private Long ts;

    /**
     * 充值唯一ID
     */
    private String seq;

    /**
     * 签名
     */
    private String sig;

}
