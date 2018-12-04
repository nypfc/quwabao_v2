package com.gedoumi.quwabao.trans.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gedoumi.quwabao.common.utils.AesCBC;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.trans.request.response.WithdrawResponse;
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

import static com.gedoumi.quwabao.common.constants.Constants.ASSET_NAME;
import static com.gedoumi.quwabao.common.constants.Constants.MEMO;

/**
 * 提现请求类
 *
 * @author Minced
 */
@Getter
public class WithdrawRequest implements GatewayRequest {

    /**
     * API URL
     */
    @JsonIgnore
    private String url;

    /**
     * API URI
     */
    @JsonIgnore
    private String uri;

    /**
     * 私钥
     */
    @JsonIgnore
    private String privateKey;

    /**
     * PFC账号
     */
    private String pfcAccount;

    /**
     * 资产名称
     */
    private String assetName = ASSET_NAME;

    /**
     * 充值金额
     */
    private String amount;

    /**
     * 备注
     * 格式为：提现目标 + 分隔符"#" + 以太坊地址
     * 提现目标目前为"erc20"
     */
    private String memo;

    /**
     * 时间戳
     */
    private Long ts = System.currentTimeMillis() / 1000;

    /**
     * 充值ID
     */
    private Long seq;

    /**
     * 构造方法，注入请求参数
     *
     * @param pfcAccount PFC账号
     * @param amount     充值金额
     * @param ethAddress 以太坊地址
     * @param seq        充值ID
     */
    public WithdrawRequest(String pfcAccount, String amount, String ethAddress, Long seq) {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("properties/gateway.properties"));
            this.url = properties.getProperty("gateway.url");
            this.uri = properties.getProperty("gateway.getEthAddress");
            this.privateKey = properties.getProperty("gateway.privateKey");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.pfcAccount = pfcAccount;
        this.amount = amount;
        this.memo = MEMO + ethAddress;
        this.seq = seq;
    }

    @Override
    public WithdrawResponse execute() {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("pfc_account", this.pfcAccount);
        paramMap.add("asset_name", this.assetName);
        paramMap.add("amount", this.amount);
        paramMap.add("memo", this.memo);
        paramMap.add("ts", String.valueOf(this.ts));
        paramMap.add("seq", this.seq);
        paramMap.add("sig", sign());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        HttpEntity entity = new HttpEntity<>(paramMap, headers);
        String response = new RestTemplate().postForObject(this.url + this.uri, entity, String.class);
        return JsonUtil.jsonToPojo(response, WithdrawResponse.class);
    }

    @Override
    public String sign() {
        StringBuilder params = new StringBuilder(this.uri);
        params.append("amount").append(this.amount);
        params.append("asset_name").append(this.assetName);
        params.append("memo").append(this.memo);
        params.append("pfc_account").append(this.pfcAccount);
        params.append("seq").append(this.seq);
        params.append("ts").append(this.ts);
        String sign = null;
        try {
            sign = AesCBC.encrypt(params.toString(), this.privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

}
