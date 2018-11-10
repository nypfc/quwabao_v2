package com.pfc.quwabao.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pfc.quwabao.sys.dataobj.model.SysSms;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * 短信Mapper
 *
 * @author Minced
 */
@Mapper
public interface SysSmsMapper extends BaseMapper<SysSms> {

    /**
     * 查询用户当日短信数量
     *
     * @param mobile    手机号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 当日短信数量
     */
    Integer smsCurrentDayCount(String mobile, Date startTime, Date endTime);

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
