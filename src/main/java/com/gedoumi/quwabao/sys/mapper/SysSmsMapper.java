package com.gedoumi.quwabao.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gedoumi.quwabao.sys.dataobj.model.SysSms;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 短信Mapper
 *
 * @author Minced
 */
@Mapper
public interface SysSmsMapper extends BaseMapper<SysSms> {

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
     * @param mobile    手机号
     * @param smsCode   短信验证码
     * @param smsType   短信类型
     * @param smsStatus 短信状态
     * @return 短信创建时间
     */
    Date checkSms(String mobile, String smsCode, Integer smsType, Integer smsStatus);

    /**
     * 更新短息状态
     *
     * @param mobile       手机号
     * @param queryStatus  查询条件状态
     * @param updateStatus 待更新的状态
     * @param updateTime   更新时间
     */
    void updateSmsStatus(String mobile, Integer queryStatus, Integer updateStatus, Date updateTime);

}
