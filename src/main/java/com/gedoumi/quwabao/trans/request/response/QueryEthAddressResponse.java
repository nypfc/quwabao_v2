package com.gedoumi.quwabao.trans.request.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询以太坊地址响应
 *
 * @author Minced
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryEthAddressResponse extends GatewayResponse {

    /**
     * 响应数据
     */
    private ResponseData data;

    /**
     * 响应数据
     *
     * @author Minced
     */
    @Data
    private class ResponseData {

        /**
         * 账号名
         */
        private String pfc_account;

        /**
         * 以太坊地址
         */
        private String eth_address;

    }

}
