package com.pfc.quwabao.common.utils;

import java.util.Date;

/**
 * 日期工具类
 *
 * @author Minced
 */
public final class DateUtil {

    private DateUtil() {
    }

    /**
     * 获取两个日期相差的秒数
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return 相差的秒数
     */
    public static long timeDiffSec(Date startTime, Date endTime) {
        return (endTime.getTime() / 1000) - (startTime.getTime() / 1000);
    }

}
