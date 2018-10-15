package com.gedoumi.quwabao.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 产生竞猜概率生成工具类
 * @author Minced
 */
@Slf4j
public final class GuessProbabilityUtil {

    private static Random random = new Random();

    /**
     * 私有化工具类构造方法
     */
    private GuessProbabilityUtil() {
    }

    /**
     * 递归生成玩法一的集合
     */
    private static LinkedHashSet<Integer> genSet1(int min, int max) {
        // 总共6个数
        int num = 6;

        int left = 100;
        LinkedHashSet<Integer> probabilityList = new LinkedHashSet<>();
        while (probabilityList.size() < num - 1) {
            int probability = random.nextInt(max + 1 - min) + min;
            probabilityList.add(probability);
            left -= probability;
        }

        if (left >= min && left < max)
            probabilityList.add(left);

        int sum = probabilityList.stream().mapToInt(Integer::intValue).sum();

        if (sum != 100)
            probabilityList = genSet1(min, max);
        else if (probabilityList.size() != num)  // 有可能第五个数字生成后直接概率为100%，所以需要验证集合的长度
            probabilityList = genSet1(min, max);

        return probabilityList;
    }

    /**
     * 递归修改玩法二值，让集合所有元素和为100
     */
    private static List<Integer> nui(List<Integer> collection, int min, int max, int differ) {
        /*
          循环修改每个值
          相差大于0，修改每个大于"最小值"的值
          相差小于0，修改每个小于"最大值"的值
          如果相差为0时，返回
         */
        for (int i = 0; i < collection.size(); i++) {
            if (differ == 0) return collection;
            Integer integer = collection.get(i);
            if (differ > 0) {
                if (integer > min) {
                    collection.set(i, integer - 1);
                    differ -= 1;
                }
            } else {
                if (integer < max) {
                    collection.set(i, integer + 1);
                    differ += 1;
                }
            }
        }
        // 循环一圈不为0，递归
        if (differ != 0)
            collection = nui(collection, min, max, differ);

        return collection;
    }

    /**
     * 玩法一：
     * 6个车猜冠军
     * 一共6个概率
     * 总概率100
     */
    public static List<BigDecimal> mode1(BigDecimal odds, int min, int max) {
        // 产生并返回概率集合
        LinkedHashSet<Integer> probabilityList = genSet1(min, max);

        log.debug("概率集合：{}，总概率：{}", probabilityList, probabilityList.stream().mapToInt(Integer::intValue).sum());

        return getOddsList(probabilityList, odds);
    }

    /**
     * 玩法二：
     * 6个车猜前二名
     * 一共15个概率
     * 总概率100
     */
    public static List<BigDecimal> mode2(BigDecimal odds, int min, int max) {
        // 15个数
        int num = 15;

        // 产生概率集合
        List<Integer> probabilityList = new ArrayList<>();
        int j = 0;
        while (j < num) {
            int probability = random.nextInt(max + 1 - min) + min;
            probabilityList.add(probability);
            j++;
        }

        // 判断总和与100的差
        int differ = probabilityList.stream().mapToInt(Integer::intValue).sum() - 100;
        // 如果不为0，递归修改元素值
        if (differ != 0)
            probabilityList = nui(probabilityList, min, max, differ);

        log.debug("概率集合：{}，总概率：{}", probabilityList, probabilityList.stream().mapToInt(Integer::intValue).sum());

        // 返回概率集合
        return getOddsList(probabilityList, odds);
    }

    /**
     * 玩法三：
     * 6个车猜两个队哪个队赢，135为一队、246为一队
     * 一共2个概率
     * 总概率100
     */
    public static List<BigDecimal> mode3(BigDecimal odds, int min, int max) {

        // 产生概率集合
        int left = 100;
        List<Integer> probabilityList = new ArrayList<>();
        int p1 = random.nextInt(max + 1 - min) + min;
        int p2 = left - p1;
        probabilityList.add(p1);
        probabilityList.add(p2);

        log.debug("概率集合：{}，总概率：{}", probabilityList, probabilityList.stream().mapToInt(Integer::intValue).sum());

        // 返回概率集合
        return getOddsList(probabilityList, odds);
    }

    /**
     * 获取赔率字符串
     * @param collection 集合
     * @param odds 赔付率
     * @return 赔率字符串
     */
    private static List<BigDecimal> getOddsList(Collection<Integer> collection, BigDecimal odds) {
        // 计算赔率：赔付率 / 几率
        return collection.stream().map(integer -> {
            BigDecimal bigDecimal1 = new BigDecimal(integer);
            BigDecimal bigDecimal2 = new BigDecimal(100);
            BigDecimal probability = bigDecimal1.divide(bigDecimal2, 2, BigDecimal.ROUND_HALF_UP);
            return odds.divide(probability, 1, BigDecimal.ROUND_HALF_UP);
        }).collect(Collectors.toList());
    }

}
