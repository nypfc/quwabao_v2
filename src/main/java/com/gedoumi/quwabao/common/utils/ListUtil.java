package com.gedoumi.quwabao.common.utils;

import java.util.List;

/**
 * 集合工具类
 * @author Minced
 */
public final class ListUtil {

    private ListUtil() {
    }

    /**
     * 集合两个元素对调
     * @param list 集合
     * @param oldPosition 原位置索引
     * @param newPosition 新位置索引
     * @param <T> 集合泛型
     */
    public static <T> void swap(List<T> list, int oldPosition, int newPosition){
        if(null == list){
            throw new IllegalStateException("The list can not be empty...");
        }
        T tempElement = list.get(oldPosition);

        // 向前移动，前面的元素需要向后移动
        if(oldPosition < newPosition){
            for(int i = oldPosition; i < newPosition; i++){
                list.set(i, list.get(i + 1));
            }
            list.set(newPosition, tempElement);
        }
        // 向后移动，后面的元素需要向前移动
        if(oldPosition > newPosition){
            for(int i = oldPosition; i > newPosition; i--){
                list.set(i, list.get(i - 1));
            }
            list.set(newPosition, tempElement);
        }
    }

}
