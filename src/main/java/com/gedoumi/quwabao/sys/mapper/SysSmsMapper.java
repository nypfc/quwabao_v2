package com.gedoumi.quwabao.sys.mapper;

import com.gedoumi.quwabao.sys.dataobj.model.SysSms;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface SysSmsMapper {

    /**
     * 创建短信
     *
     * @param sms 短信对象
     */
    void createSysSms(SysSms sms);

    /**
     * 查询当日短信创建时间
     *
     * @param mobile    手机号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 日期集合
     */
    List<Date> smsCurrentDayCount(String mobile, Date startTime, Date endTime);

    /**
     * 验证短信验证码
     *
     * @param mobile  手机号
     * @param smsCode 短信验证码
     * @param smsType 短信类型
     * @return 短信创建时间
     */
    Date checkSms(String mobile, String smsCode, Integer smsType);

    /**
     * 更新短息状态
     *
     * @param mobile     手机号
     * @param updateTime 更新时间
     */
    void updateSmsStatus(String mobile, Date updateTime);

}
