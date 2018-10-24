package com.gedoumi.quwabao.common.utils;

import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 当日的开始时间与结束时间
 *
 * @author Minced
 */
@Getter
public final class CurrentDateUtil {

    /**
     * 当日开始时间
     */
    private Date startTime;

    /**
     * 当日结束时间
     */
    private Date endTime;

    /**
     * 构造方法
     * 获取到当前日期的0点与隔天日期的0点
     */
    public CurrentDateUtil() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = sdf.parse(sdf.format(new Date()));
            this.startTime = startTime;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            this.endTime = calendar.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

}
