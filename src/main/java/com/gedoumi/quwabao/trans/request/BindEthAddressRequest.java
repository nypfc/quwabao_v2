package com.gedoumi.quwabao.trans.request;

import com.gedoumi.quwabao.common.utils.AesCBC;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.trans.request.GatewayRequest;
import com.gedoumi.quwabao.trans.request.response.BindEthAddressResponse;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Properties;

/**
 * 绑定以太坊地址
 *
 * @author Minced
 */
@Getter
public class BindEthAddressRequest implements GatewayRequest {

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
    public BindEthAddressRequest(String pfcAccount) {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("properties/gateway.properties"));
            this.url = properties.getProperty("gateway.url");
            this.uri = properties.getProperty("gateway.bindEthAddress");
            this.privateKey = properties.getProperty("gateway.privateKey");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.pfcAccount = pfcAccount;
    }

    @Override
    public BindEthAddressResponse execute() {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("pfc_account", this.pfcAccount);
        paramMap.add("ts", String.valueOf(this.ts));
        paramMap.add("sig", sign());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        HttpEntity entity = new HttpEntity<>(paramMap, headers);
        String response = new RestTemplate().postForObject(this.url + this.uri, entity, String.class);
        return JsonUtil.jsonToPojo(response, BindEthAddressResponse.class);
    }

    @Override
    public String sign() {
        StringBuilder params = new StringBuilder(this.uri);
        params.append("pfc_account").append(this.pfcAccount).append("ts").append(this.ts);
        try {
            return AesCBC.encrypt(params.toString(), this.privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
