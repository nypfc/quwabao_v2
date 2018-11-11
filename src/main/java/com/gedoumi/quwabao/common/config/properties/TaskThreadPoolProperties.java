package com.gedoumi.quwabao.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 线程池配置
 *
 * @author Minced
 */
@Getter
@Setter
@ConfigurationProperties("task.pool")
@EnableConfigurationProperties(TaskThreadPoolProperties.class)
public class TaskThreadPoolProperties {

    /**
     * 核心线程池大小
     */
    private Integer corePoolSize;

    /**
     * 最大线程池
     */
    private Integer maxPoolSize;

    /**
     * 存活时间
     */
    private Integer keepAliveSecond;

    /**
     * 队列容量
     */
    private Integer queueCapacity;

    /**
     * 线程名前缀
     */
    private String threadNamePrefix;

}
