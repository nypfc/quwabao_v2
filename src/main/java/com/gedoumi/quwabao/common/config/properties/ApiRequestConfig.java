package com.gedoumi.quwabao.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 交易所请求API地址
 *
 * @author Minced
 */
@Getter
@Setter
@ConfigurationProperties("gateway")
@EnableConfigurationProperties(ApiRequestConfig.class)
public class ApiRequestConfig {

    /**
     * 请求地址
     */
    private String url;

    /**
     * 交易所私钥
     */
    private String privateKey;

    /**
     * 获取以太坊API
     */
    private String getEthAddress;

    /**
     * 绑定以太坊API
     */
    private String bindEthAddress;

    /**
     * 提现API
     */
    private String withdraw;

}
