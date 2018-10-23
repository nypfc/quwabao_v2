package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.config.properties.SMSProperties;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.SmsType;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.CodeUtils;
import com.gedoumi.quwabao.common.utils.CurrentDate;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.dataobj.model.SysSms;
import com.gedoumi.quwabao.sys.mapper.SysSmsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;


/**
 * 系统短信Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class SysSmsService {

    @Resource
    private SysSmsMapper sysSmsMapper;

    @Resource
    private SMSProperties smsProperties;

    @Resource
    private RedisCache redisCache;

    /**
     * 发送短信
     *
     * @param sendType 类型
     * @param mobile   手机号
     * @param code     验证码
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void sendSms(String sendType, String mobile, String code) {

        // =========== 1.验证 ===========

        // 类型验证
        int type;  // 短信类型参数
        if (StringUtils.equals(SmsType.Register.getName(), sendType)) {
            type = SmsType.Register.getValue();
        } else if (StringUtils.equals(SmsType.Login.getName(), sendType)) {
            type = SmsType.Login.getValue();
        } else if (StringUtils.equals(SmsType.ResetPswd.getName(), sendType)) {
            type = SmsType.ResetPswd.getValue();
        } else {
            log.error("短信类型参数错误，sendType:{}", sendType);
            throw new BusinessException(CodeEnum.SendSMSError);
        }
        // 图片验证码验证
        String cacheValidateCode = (String) redisCache.getKeyValueData("vCode:" + mobile);
        if (cacheValidateCode == null) {
            log.error("未获取到缓存验证码，cacheValidateCode:null");
            throw new BusinessException(CodeEnum.ValidateCodeExpire);
        }
        if (!StringUtils.equals(cacheValidateCode, code.toUpperCase())) {
            log.error("缓存的验证码:{}与参数验证码:{}不匹配", cacheValidateCode, code);
            throw new BusinessException(CodeEnum.ValidateCodeError);
        }
        // 当日短信上限验证
        CurrentDate currentDate = new CurrentDate();
        Integer count = sysSmsMapper.smsCurrentDayCount(mobile, currentDate.getStartTime(), currentDate.getEndTime());
        if (count >= Constants.SMS_DAY_COUNT) {
            log.error("手机号:{}当日验证码数量已达上限", mobile);
            throw new BusinessException(CodeEnum.SmsCountError);
        }

        // =========== 2.发送短信 ===========

        // 生成短信验证码
        String smsCode = CodeUtils.generateSMSCode();
        // 获取短信参数
        String url = smsProperties.getUrl();
        String username = smsProperties.getUsername();
        String password = smsProperties.getPassword();
        String content = smsProperties.getContent(smsCode);
        // 参数Map
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("url", url);
        paramMap.add("username", username);
        paramMap.add("password", password);
        paramMap.add("mobile", mobile);
        paramMap.add("content", content);
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");  // post请求默认为application/json方式，改为拼接key=value的形式
        HttpEntity requestEntity = new HttpEntity<>(paramMap, headers);
        // 发送Post请求
        String ret = new RestTemplate().postForObject(url, requestEntity, String.class);
        // 验证结果
        log.info("ret:" + ret);
        if (StringUtils.isEmpty(ret)) {
            log.error("返回结果为空，ret:{}", ret);
            throw new BusinessException(CodeEnum.SendSMSError);
        }
        String[] retArr = ret.split(",");
        if (!StringUtils.equals(retArr[0], "1")) {
            log.error("手机号:{}发送短信失败，状态码:{}，信息:{}", mobile, retArr[0], retArr[1]);
            throw new BusinessException(CodeEnum.SendSMSError);
        }

        // =========== 3.创建短信 ===========

        SysSms sms = new SysSms();
        sms.setCode(smsCode);
        sms.setSmsType(type);
        sms.setMobilePhone(mobile);
        sysSmsMapper.createSysSms(sms);
    }

    /**
     * 查询短信验证码
     *
     * @param mobilePhone 手机号
     * @param smsCode     短信验证码
     * @param type        类型
     * @return 短信创建日期
     */
    public Date getSms(String mobilePhone, String smsCode, Integer type) {
        return sysSmsMapper.checkSms(mobilePhone, smsCode, type);
    }

    /**
     * 更新短信状态
     *
     * @param mobile 手机号
     */
    public void updateSmsStatus(String mobile) {
        sysSmsMapper.updateSmsStatus(mobile);
    }

}
