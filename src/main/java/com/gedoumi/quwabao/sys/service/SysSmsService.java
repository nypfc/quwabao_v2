package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.SmsStatusEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.CodeUtils;
import com.gedoumi.quwabao.common.utils.CurrentDateUtil;
import com.gedoumi.quwabao.component.RedisCache;
import com.gedoumi.quwabao.sys.dataobj.model.SysConfig;
import com.gedoumi.quwabao.sys.dataobj.model.SysSms;
import com.gedoumi.quwabao.sys.mapper.SysSmsMapper;
import com.gedoumi.quwabao.sys.request.SMSRequest;
import com.gedoumi.quwabao.sys.request.response.SMSResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


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
    private SysConfigService sysConfigService;

    @Resource
    private RedisCache redisCache;

    /**
     * 发送短信
     *
     * @param sendType 类型
     * @param zone     国家编码
     * @param mobile   手机号
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendSms(Integer sendType, String zone, String mobile) {
        // 获取系统配置
        SysConfig sysConfig = sysConfigService.getSysConfig();
        // 当日短信上限验证
        CurrentDateUtil dateUtil = new CurrentDateUtil();
        Integer count = sysSmsMapper.smsCurrentDayCount(mobile, dateUtil.getStartTime(), dateUtil.getEndTime());
        if (count >= sysConfig.getSmsDayCount()) {
            log.error("手机号:{}当日验证码数量已达上限", mobile);
            throw new BusinessException(CodeEnum.SmsCountError);
        }
        // 判断是否重复发短信
        if (redisCache.getKeyValueData("sms:" + mobile) != null) {
            log.error("手机号:{}重复发送短信", mobile);
            throw new BusinessException(CodeEnum.RepeatedlySMS);
        }
        // 生成短信验证码
        String smsCode = CodeUtils.generateSMSCode();
        // 发送Post请求
        SMSRequest smsRequest = new SMSRequest(mobile, smsCode);
        SMSResponse smsResponse = Optional.ofNullable(smsRequest.execute()).orElseThrow(() -> {
            log.error("smsResponse返回结果为空");
            return new BusinessException(CodeEnum.SendSMSError);
        });
        // 验证结果
        log.info("smsResponse:" + smsResponse);
        if (!StringUtils.equals(smsResponse.getCode(), "1")) {
            log.error("手机号:{}发送短信失败，状态码:{}，信息:{}", mobile, smsResponse.getCode(), smsResponse.getContent());
            throw new BusinessException(CodeEnum.SendSMSError);
        }
        // 创建短信
        updateSmsStatus(mobile);  // 将其他短信置为失效
        SysSms sms = new SysSms();
        Date now = new Date();
        sms.setCode(smsCode);
        sms.setSmsStatus(SmsStatusEnum.Enable.getValue());
        sms.setSmsType(sendType);
        sms.setMobilePhone(mobile);
        sms.setCreateTime(now);
        sms.setUpdateTime(now);
        sysSmsMapper.insert(sms);
        // 缓存短信
        String key = "sms:" + mobile;
        redisCache.setExpireKeyValueData(key, sms, (long) sysConfig.getSmsExpiredSecond(), TimeUnit.SECONDS);
    }

    /**
     * 更新短信状态
     *
     * @param mobile 手机号
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSmsStatus(String mobile) {
        sysSmsMapper.updateSmsStatus(mobile, SmsStatusEnum.Enable.getValue(), SmsStatusEnum.Disable.getValue(), new Date());
    }

}
