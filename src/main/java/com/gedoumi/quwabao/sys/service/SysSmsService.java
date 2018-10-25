package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.config.properties.SMSProperties;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.CodeUtils;
import com.gedoumi.quwabao.common.utils.CurrentDateUtil;
import com.gedoumi.quwabao.common.utils.DateUtil;
import com.gedoumi.quwabao.sys.dataobj.model.SysSms;
import com.gedoumi.quwabao.sys.mapper.SysSmsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


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
     * @param sendType 类型
     * @param mobile   手机号
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendSms(Integer sendType, String mobile) {
        // =========== 1.验证 ===========
        // 判断是否重复发短信
        CurrentDateUtil dateUtil = new CurrentDateUtil();
        List<Date> dates = sysSmsMapper.smsCurrentDayCount(mobile, dateUtil.getStartTime(), dateUtil.getEndTime());
        if (!CollectionUtils.isEmpty(dates)) {
            if (DateUtil.timeDiffSec(dates.get(0), new Date()) <= smsProperties.getIntervalSecond()) {
                log.error("手机号:{}重复发短信", mobile);
                throw new BusinessException(CodeEnum.RepeatedlySMS);
            }
        }
        // 当日短信上限验证
        if (dates.size() >= smsProperties.getDayCount()) {
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
        // RestTemplate的post请求方式默认为application/json方式，改为拼接key=value的形式
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
        sms.setSmsType(sendType);
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
        sysSmsMapper.updateSmsStatus(mobile, new Date());
    }

}
