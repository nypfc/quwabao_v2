package com.gedoumi.quwabao.component;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存
 *
 * @author Minced
 */
@Component
public class RedisCache {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取Key-Value
     *
     * @param key 键
     * @return data
     */
    public Object getKeyValueData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置Key-Value
     *
     * @param key   键
     * @param value 数据
     */
    public void setKeyValueData(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置带过期时间的Key-Value
     *
     * @param key      键
     * @param value    数据
     * @param time     时间
     * @param timeUnit 时间单位
     */
    public void setExpireKeyValueData(String key, Object value, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 删除Key-Value
     *
     * @param key 键
     */
    public void deleteKeyValueData(String key) {
        redisTemplate.delete(key);
    }

}
