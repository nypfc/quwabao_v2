package com.gedoumi.quwabao.common.config;

import com.gedoumi.quwabao.common.config.properties.TaskThreadPoolProperties;
import com.gedoumi.quwabao.sys.service.SmsListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Redis配置类
 *
 * @author Minced
 */
@Configuration
public class RedisConfig {

    /**
     * Redis序列化配置
     *
     * @param redisConnectionFactory Redis连接工厂
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    /**
     * Redis消息监听容器配置
     *
     * @param redisConnectionFactory   Redis连接工厂
     * @param taskThreadPoolProperties 线程池配置类
     * @param smsListener              短信监听类
     * @return Redis消息监听容器
     */
    @Bean
    public RedisMessageListenerContainer configRedisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, TaskThreadPoolProperties taskThreadPoolProperties, SmsListener smsListener) {
        // 创建线程池
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(taskThreadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(taskThreadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(taskThreadPoolProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(taskThreadPoolProperties.getKeepAliveSecond());
        executor.setThreadNamePrefix(taskThreadPoolProperties.getThreadNamePrefix());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        // 创建消息监听容器
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);  // 设置Redis的连接工厂
        container.setTaskExecutor(executor);  // 设置监听使用的线程池
        container.addMessageListener(smsListener, new ChannelTopic("__keyevent@0__:expired"));  // 设置监听器和监听的Topic，__keyevent@<db>__:expired为固定格式，<db>为监听的仓库号，expired表示要监听过期事件
        return container;
    }

}
