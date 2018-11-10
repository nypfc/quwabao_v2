package com.pfc.quwabao.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Json工具类
 *
 * @author Minced
 */
public final class JsonUtil {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtil() {
    }

    /**
     * 将对象转换成Json字符串
     *
     * @param data 待转换的对象
     * @return Json数据
     */
    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Json结果集转化为对象
     *
     * @param jsonData Json数据
     * @param beanType 对象中的object类型
     * @return 转换后的对象
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Json数据转换成pojo对象list
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return 转换后的对象集合
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
