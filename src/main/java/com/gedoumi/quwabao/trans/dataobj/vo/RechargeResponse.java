package com.gedoumi.quwabao.trans.dataobj.vo;

import lombok.Data;

/**
 * 充值回调响应
 *
 * @author Mincd
 */
@Data
public class RechargeResponse {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private Object data;

}
