package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.SmsStatusEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.CodeUtils;
import com.gedoumi.quwabao.common.utils.CurrentDateUtil;
import com.gedoumi.quwabao.common.component.RedisCache;
import com.gedoumi.quwabao.sys.dataobj.form.SendSMSForm;
import com.gedoumi.quwabao.sys.dataobj.model.SysConfig;
import com.gedoumi.quwabao.sys.dataobj.model.SysSms;
import com.gedoumi.quwabao.sys.mapper.SysSmsMapper;
import com.gedoumi.quwabao.sys.request.InterSMSRequest;
import com.gedoumi.quwabao.sys.request.SMSRequest;
import com.gedoumi.quwabao.sys.request.response.InterSMSResponse;
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
    private SysZoneService sysZoneService;

    @Resource
    private RedisCache redisCache;

    /**
     * 发送短信
     *
     * @param sendSMSForm 发送短信表单
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendSms(SendSMSForm sendSMSForm) {
        // 获取参数
        String zone = sendSMSForm.getZone();
        String mobile = sendSMSForm.getMobile();
        Integer sendType = Integer.parseInt(sendSMSForm.getType());
        // 验证区号
        Integer zoneCount = sysZoneService.countSysZone(zone);
        if (zoneCount == null || zoneCount == 0) {
            log.error("国家编码:{}不存在", zone);
            throw new BusinessException(CodeEnum.ZoneError);
        }
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
        // 发送请求，如果区号是中国，使用旧API，否则使用新API
        if (StringUtils.equals("86", zone)) {
            log.info("use old sms api");
            SMSRequest smsRequest = new SMSRequest(mobile, smsCode);
            SMSResponse smsResponse = Optional.ofNullable(smsRequest.execute()).orElseThrow(() -> {
                log.error("smsResponse返回结果为空");
                return new BusinessException(CodeEnum.SendSMSError);
            });
            // 验证结果
            log.info("smsResponse:" + smsResponse);
            if (!StringUtils.equals(smsResponse.getCode(), SMSResponse.SUCCESS)) {
                log.error("手机号:{}发送短信失败，状态码:{}，信息:{}", mobile, smsResponse.getCode(), smsResponse.getContent());
                throw new BusinessException(CodeEnum.SendSMSError);
            }
        } else {
            log.info("use new sms api");
            InterSMSRequest smsRequest = new InterSMSRequest(zone, mobile, smsCode);
            InterSMSResponse smsResponse = Optional.ofNullable(smsRequest.execute()).orElseThrow(() -> {
                log.error("smsResponse返回结果为空");
                return new BusinessException(CodeEnum.SendSMSError);
            });
            // 验证结果
            log.info("smsResponse:" + smsResponse);
            if (!StringUtils.equals(smsResponse.getCode(), InterSMSResponse.SUCCESS)) {
                log.error("手机号:{}发送短信失败，状态码:{}，信息:{}", mobile, smsResponse.getCode(), smsResponse.getError());
                throw new BusinessException(CodeEnum.SendSMSError);
            }
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
        redisCache.setExpireKeyValueData(genKey(mobile), sms, (long) sysConfig.getSmsExpiredSecond(), TimeUnit.SECONDS);
    }

    /**
     * 验证短信
     *
     * @param mobile  手机号
     * @param smsCode 短信验证码
     * @param smsType 短信类型
     * @return 成功返回true，失败返回false
     */
    public Boolean validateSms(String mobile, String smsCode, Integer smsType) {
        String key = genKey(mobile);
        SysSms sysSms = (SysSms) redisCache.getKeyValueData(key);
        if (sysSms == null || !sysSms.getSmsType().equals(smsType) || !sysSms.getCode().equals(smsCode)) {
            return false;
        }
        updateSmsStatus(mobile);  // 短信置为失效
        redisCache.deleteKeyValueData(key);  // 删除缓存
        return true;
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

    /**
     * 产生短信Key
     *
     * @param mobile 手机号
     * @return key
     */
    private String genKey(String mobile) {
        return String.format("sms:%s", mobile);
    }

}
