package com.gedoumi.quwabao.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
public class SmsUtil {

    private final static String CONTENT = "验证码：%s(1分钟内有效)，请勿告知他人。用于您本次登录和使用趣挖宝APP，避免因使用其他应用造成信息泄露。【趣挖宝】";

    public static void sendReg(String mobile, String code) {

        String content = String.format(CONTENT, code);
        String url = "http://www.api.zthysms.com/sendSms.do";
        String username = "xiangqiandaihy";  //账e号账号：yingdinghy 密码：e0pHmm
        String password = "65tkXZ";  //密码

        //账号：xiangqiandaihy 密码：65tkXZ 这个是正式账号
        String xh = "";
        try {
            content = URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String param = "url=" + url + "&username=" + username + "&password=" + MD5EncryptUtil.md5Encrypy(password) + "&mobile=" + mobile + "&content=" + content + "&xh=" + xh;
        String ret = HttpRequest.sendPost(url, param);  // 定时信息只可为post方式提交
        log.info("ret:" + ret);
        log.info(param);
    }

    /**
     * 生成6位短信验证码
     *
     * @return 短信验证码
     */
    public static String generateCode() {
        return String.format("%06d", NumberUtil.randomInt(0, 999999));
    }

}
