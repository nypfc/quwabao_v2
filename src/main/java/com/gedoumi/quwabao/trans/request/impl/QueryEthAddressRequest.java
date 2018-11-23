package com.gedoumi.quwabao.trans.request.impl;

import com.gedoumi.quwabao.common.utils.AesCBC;
import com.gedoumi.quwabao.trans.request.GatewayRequest;
import lombok.Getter;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 查询以太坊地址
 *
 * @author Minced
 */
@Getter
public class QueryEthAddressRequest implements GatewayRequest {

    /**
     * API URL
     */
    private String url;

    /**
     * API URI
     */
    private String uri;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * PFC账号
     */
    private String pfcAccount;

    /**
     * 时间戳
     */
    private Long ts = System.currentTimeMillis() / 1000;

    /**
     * 构造方法，注入请求参数
     *
     * @param pfcAccount PFC账号
     */
    public QueryEthAddressRequest(String pfcAccount) {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("properties/gateway.properties"));
            this.url = properties.getProperty("gateway.url");
            this.uri = properties.getProperty("gateway.getEthAddress");
            this.privateKey = properties.getProperty("gateway.privateKey");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.pfcAccount = pfcAccount;
    }

    @Override
    public String execute() {
        String url = getUrl() + getUri() + "?pfc_account={1}&ts={2}&sig={3}";
        return new RestTemplate().getForObject(url, String.class, getPfcAccount(), String.valueOf(ts), sign());
    }

    @Override
    public String sign() {
        StringBuilder params = new StringBuilder(getUri());
        params.append("pfc_account").append(getPfcAccount());
        params.append("ts").append(ts);
        try {
            return AesCBC.encrypt(params.toString(), this.privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
