package com.gedoumi.quwabao.trans.dataobj.dto;

import lombok.Data;

/**
 * 充值成功响应内容
 *
 * @author Minced
 */
@Data
public class RechargeResponseData {

    /**
     * PFC账号
     */
    private String pfc_account;

    /**
     * 以太坊地址
     */
    private String eth_address;

}
