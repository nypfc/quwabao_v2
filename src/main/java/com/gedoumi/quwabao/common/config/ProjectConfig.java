package com.gedoumi.quwabao.common.config;

import com.gedoumi.quwabao.common.config.properties.SMSProperties;
import com.gedoumi.quwabao.common.config.properties.TaskThreadPoolProperties;
import com.gedoumi.quwabao.component.ApiInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 项目相关配置
 *
 * @author Minced
 */
@Configuration
public class ProjectConfig implements WebMvcConfigurer {

    /**
     * 注册Api拦截器
     *
     * @return Api请求拦截器
     */
    @Bean
    public ApiInterceptor apiRequestInterceptor() {
        return new ApiInterceptor();
    }

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 令牌验证拦截器
        registry.addInterceptor(apiRequestInterceptor())
                .addPathPatterns("/v2/user/**")
                .addPathPatterns("/v2/trans/**")
                .addPathPatterns("/v2/rent")
                // 验证接口不需要拦截
                .excludePathPatterns("/v2/user/check/**")
                // 注册接口不需要拦截
                .excludePathPatterns("/v2/user/register")
                // 重置密码（忘记密码）接口不需要拦截
                .excludePathPatterns("/v2/user/password/reset");
    }

    /**
     * 跨域过滤器配置
     *
     * @return Filter注册器
     */
    @Bean
    @SuppressWarnings("unchecked")
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);  // 允许跨域缓存
        config.setMaxAge(3600L);  // 缓存时间（单位：秒）
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);  // 跨域Filter排序，排到第一个
        return bean;
    }

    /**
     * 短信配置
     *
     * @return 短信属性类
     */
    @Bean
    public SMSProperties smsProperties() {
        return new SMSProperties();
    }

    /**
     * 线程池配置
     *
     * @return 线程池属性类
     */
    @Bean
    public TaskThreadPoolProperties taskThreadPoolProperties() {
        return new TaskThreadPoolProperties();
    }

}
