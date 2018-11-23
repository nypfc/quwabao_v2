package com.gedoumi.quwabao.trans.request;

/**
 * 交易所请求
 *
 * @author Minced
 */
public interface GatewayRequest {

    /**
     * 执行请求
     *
     * @return 请求结果
     */
    String execute();

    /**
     * 计算签名
     *
     * @return 签名字符串
     */
    String sign();

}
