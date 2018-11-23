package com.gedoumi.quwabao.sys.request;

import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.sys.request.response.SMSResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Properties;

/**
 * 短信请求类
 *
 * @author Minced
 */
@Slf4j
@Getter
public class SMSRequest {

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
     * @param mobile  手机号
     * @param smsCode 验证码
     */
    public SMSRequest(String mobile, String smsCode) {
        this.mobile = mobile;
        this.smsCode = smsCode;
    }

    /**
     * 执行请求
     *
     * @return 结果数据
     */
    public SMSResponse execute() {
        // 获取短信参数
        String url = "";
        MultiValueMap<String, String> paramMap = null;
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("properties/sms.properties"));
            url = properties.getProperty("sms.url");
            // 参数Map
            paramMap = new LinkedMultiValueMap<>();
            paramMap.add("url", url);
            paramMap.add("username", properties.getProperty("sms.username"));
            paramMap.add("password", MD5EncryptUtil.md5Encrypy(properties.getProperty("sms.password")));
            paramMap.add("mobile", this.mobile);
            paramMap.add("content", getContent(this.smsCode));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        HttpEntity requestEntity = new HttpEntity<>(paramMap, headers);
        // 发送Post请求
        String responseStr = new RestTemplate().postForObject(url, requestEntity, String.class);
        // 封装返回信息
        if (StringUtils.isEmpty(responseStr))
            return null;
        String[] responseStrArr = responseStr.split(",");
        SMSResponse smsResponse = new SMSResponse();
        smsResponse.setCode(responseStrArr[0]);
        smsResponse.setContent(responseStrArr[1]);
        return smsResponse;
    }

    /**
     * 获取短信内容
     *
     * @param code 验证码
     * @return 短信内容
     */
    private String getContent(String code) {
        String content = "验证码：%s(1分钟内有效)，请勿告知他人。用于您本次登录和使用趣挖宝APP，避免因使用其他应用造成信息泄露。【趣挖宝】";
        return String.format(content, code);
    }

}
