package com.gedoumi.quwabao.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsUtil {

    static Logger LOGGER = LoggerFactory.getLogger(SmsUtil.class);

    final static String content = "验证码：%s(1分钟内有效)，请勿告知他人。用于您本次登录和使用趣挖宝APP，避免因使用其他应用造成信息泄露。【趣挖宝】";

    public static void sendReg(String mobile, String code) throws InterruptedException {
        send(mobile,String.format(content,code));
    }

    public static void send(String mobile, String content) throws InterruptedException {
        String url="http://www.api.zthysms.com/sendSms.do";
        String username = "xiangqiandaihy";  //账e号账号：yingdinghy 密码：e0pHmm
        String password = "65tkXZ";  //密码
        //账号：xiangqiandaihy 密码：65tkXZ 这个是正式账号
        String tkey = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
//        String mobile="";  //发送的手机号
//        String content="";   //内容

        //String time="2016-09-06 17:48:22";//定时信息所需参数时间格式为yyyy-MM-dd HH:mm:ss
        String xh="";
        try {
            content=URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String param="url="+url+"&username="+username+"&password="+MD5Gen.getMD5(password)+"&mobile="+mobile+"&content="+content+"&xh="+xh;
        //String param="url="+url+"&username="+username+"&password="+MD5Gen.getMD5(MD5Gen.getMD5(password)+tkey)+"&tkey="+tkey+"&time="+time+"&mobile="+mobile+"&content="+content+"&xh="+xh;//定时信息url输出
        String ret=HttpRequest.sendPost(url,param);//定时信息只可为post方式提交
        LOGGER.info("ret:"+ret);
        LOGGER.info(param);

    }

    public static String generateCode(){
        return String.format("%06d",NumberUtil.randomInt(0,999999));
    }

    public static void main(String[] args){
        System.out.println(String.format("%06d",9898));
    }

    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            LOGGER.warn("手机号应为11位数");
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {
                LOGGER.warn("请填入正确的手机号");
            }
            return isMatch;
        }
    }

}
