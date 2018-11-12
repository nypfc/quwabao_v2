package com.gedoumi.quwabao.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 交易所私钥
 *
 * @author Minced
 */
@Getter
@Setter
@ConfigurationProperties("sys")
@EnableConfigurationProperties(PrivateKeyProperties.class)
public class PrivateKeyProperties {

    /**
     * 私钥
     */
    private String privateKey;

}
