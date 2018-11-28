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

    /**
     * 成功的响应内容
     */
    public void success() {
        this.code = 0;
        this.msg = "ok";
    }

    /**
     * 验证失败的响应内容
     */
    public void acessError() {
        this.code = 9001;
        this.msg = "invalid access";
    }

    /**
     * 账号错误的响应内容
     */
    public void accountError() {
        this.code = 2001;
        this.msg = "unknown pfc_account";
    }

}
