package com.gedoumi.quwabao.common.config.properties;

import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 短信配置
 *
 * @author Minced
 */
@Getter
@Setter
@ConfigurationProperties("sms")
@EnableConfigurationProperties(SMSProperties.class)
public class SMSProperties {

    /**
     * 请求地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 每日短信上限
     */
    private Integer dayCount;

    /**
     * 短信间隔时间
     */
    private Integer intervalSecond;

    /**
     * 短信过期时间
     */
    private Integer expiredSecond;

    /**
     * @return MD5加密后的密码
     */
    public String getPassword() {
        return MD5EncryptUtil.md5Encrypy(password);
    }

    /**
     * 获取短信内容
     *
     * @param code 验证码
     * @return 短信内容
     */
    public String getContent(String code) {
        String content = "验证码：%s(1分钟内有效)，请勿告知他人。用于您本次登录和使用趣挖宝APP，避免因使用其他应用造成信息泄露。【趣挖宝】";
        return String.format(content, code);
    }

}
