package com.gedoumi.quwabao.common.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 计算总奖金工具类
 * @author Minced
 */
public final class BounsUtil {

    /**
     * 私有化工具类构造方法
     */
    private BounsUtil() {
    }

    public static Set<Integer> ranking() {
        // 目前生成随机1-6名
        HashSet<Integer> ranking = new HashSet<>();
        ranking.add(1);
        ranking.add(2);
        ranking.add(3);
        ranking.add(4);
        ranking.add(5);
        ranking.add(6);
        return ranking;
    }

}
