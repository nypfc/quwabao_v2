package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.base.CurrentDate;
import com.gedoumi.quwabao.sys.dataobj.model.SysSms;
import com.gedoumi.quwabao.sys.mapper.SysSmsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 创建短信验证码数据
     *
     * @param mobile 手机号
     * @param code   验证码
     * @param type   类型
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void createSysSms(String mobile, String code, Integer type) {
        SysSms sms = new SysSms();
        sms.setCode(code);
        sms.setSmsType(type);
        sms.setMobilePhone(mobile);
        Date now = new Date();
        sms.setCreateTime(now);
        sms.setUpdateTime(now);
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
