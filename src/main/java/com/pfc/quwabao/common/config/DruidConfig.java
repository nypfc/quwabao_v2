package com.pfc.quwabao.common.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid连接池配置
 *
 * @author Minced
 */
@Configuration
public class DruidConfig {

    /**
     * Druid连接配置
     */
    @ConfigurationProperties("spring.datasource.druid")  // 通过配置文件配置
    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        return druidDataSource;
    }

    /**
     * 记录慢SQL
     */
    @Bean
    public Filter statFilter() {
        StatFilter filter = new StatFilter();
        filter.setSlowSqlMillis(10000);  // 设置执行超过多少毫秒的SQL属于慢SQL
        filter.setLogSlowSql(true);
        filter.setMergeSql(true);
        return filter;
    }

}
