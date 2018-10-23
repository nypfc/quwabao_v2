package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.config.properties.SMSProperties;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.CurrentDate;
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

    /**
     * 发送短信
     *
     * @param mobile 手机号
     * @param code   验证码
     * @param type   类型
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void sendSms(String mobile, String code, Integer type) {
        // 获取短信参数
        String url = smsProperties.getUrl();
        String username = smsProperties.getUsername();
        String password = smsProperties.getPassword();
        String content = smsProperties.getContent(code);

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

        // 创建短信
        SysSms sms = new SysSms();
        sms.setCode(code);
        sms.setSmsType(type);
        sms.setMobilePhone(mobile);
        sysSmsMapper.createSysSms(sms);
    }

    /**
     * 获取当日短信数量
     *
     * @param mobile 手机号
     * @return 短信数量
     */
    public Integer smsCurrentDayCount(String mobile) {
        CurrentDate currentDate = new CurrentDate();
        return sysSmsMapper.smsCurrentDayCount(mobile, currentDate.getStartTime(), currentDate.getEndTime());
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
