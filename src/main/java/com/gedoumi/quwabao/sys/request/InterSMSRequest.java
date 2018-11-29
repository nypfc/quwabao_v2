package com.gedoumi.quwabao.sys.request;

import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.sys.request.response.InterSMSResponse;
import com.gedoumi.quwabao.sys.request.response.SMSResponse;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * 国际短信请求类
 *
 * @author Minced
 */
@Getter
public class InterSMSRequest {

    /**
     * 国家编码
     */
    private String zone;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 构造方法，注入请求参数
     *
     * @param zone    区号
     * @param mobile  手机号
     * @param smsCode 短信验证码
     */
    public InterSMSRequest(String zone, String mobile, String smsCode) {
        this.zone = zone;
        this.mobile = mobile;
        this.smsCode = smsCode;
    }

    /**
     * 执行请求
     */
    public InterSMSResponse execute() {
        // 获取短信参数
        String url = "";
        HashMap<String, String> paramMap;
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("properties/sms.properties"));
            url = properties.getProperty("sms.international.send.url");
            // 参数Map
            paramMap = Maps.newHashMap();
            paramMap.put("account", properties.getProperty("sms.international.account"));
            paramMap.put("password", properties.getProperty("sms.international.password"));
            paramMap.put("msg", getContent(this.smsCode));
            paramMap.put("mobile", this.mobile);
        } catch (IOException e) {
            e.printStackTrace();
            paramMap = Maps.newHashMap();
        }
        HttpEntity requestEntity = new HttpEntity<>(paramMap);
        // 发送Post请求
        String response = new RestTemplate().postForObject(url, requestEntity, String.class);
        // 封装返回数据
        if (StringUtils.isEmpty(response))
            return null;
        return JsonUtil.jsonToPojo(response, InterSMSResponse.class);
    }

    /**
     * 获取短信内容
     *
     * @param code 验证码
     * @return 短信内容
     */
    private String getContent(String code) {
        String content = "【趣挖宝】验证码：%s(1分钟内有效)，请勿告知他人。用于您本次登录和使用趣挖宝APP，避免因使用其他应用造成信息泄露。";
        return String.format(content, code);
    }

}
