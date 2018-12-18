package com.gedoumi.quwabao.common.component;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存
 *
 * @author Minced
 */
@Component
public class RedisCache {

    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperations;

    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    /**
     * 获取Key-Value
     *
     * @param key 键
     * @return data
     */
    public Object getKeyValueData(String key) {
        return this.valueOperations.get(key);
    }

    /**
     * 设置Key-Value
     *
     * @param key   键
     * @param value 数据
     */
    public void setKeyValueData(String key, Object value) {
        this.valueOperations.set(key, value);
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
        this.valueOperations.set(key, value, time, timeUnit);
    }

    /**
     * 删除Key-Value
     *
     * @param key 键
     */
    public void deleteKeyValueData(String key) {
        this.redisTemplate.delete(key);
    }

}
