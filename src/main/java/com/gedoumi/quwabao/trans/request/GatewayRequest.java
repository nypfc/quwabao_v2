package com.gedoumi.quwabao.trans.request;

import com.gedoumi.quwabao.trans.request.response.GatewayResponse;

/**
 * 交易所请求
 *
 * @author Minced
 */
public interface GatewayRequest {

    String GATEWAY_URL = "";

    /**
     * 执行请求
     *
     * @return 网关响应对象
     */
    GatewayResponse execute();

    /**
     * 计算签名
     *
     * @return 签名字符串
     */
    String sign();

}
