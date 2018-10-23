package com.gedoumi.quwabao.common.config;

import com.gedoumi.quwabao.common.config.properties.SMSProperties;
import com.gedoumi.quwabao.component.ApiInterceptor;
import com.gedoumi.quwabao.component.RealNameInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
     * 注册实名验证拦截器
     *
     * @return 实名验证拦截器
     */
    @Bean
    public RealNameInterceptor realNameInterceptor() {
        return new RealNameInterceptor();
    }

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiRequestInterceptor())
                .addPathPatterns("/v2/**")
                // 登录与注册不需要拦截
                .excludePathPatterns("/v2/login/**")
                .excludePathPatterns("/v2/register/**")
                // 重置密码不需要拦截
                .excludePathPatterns("/v2/user/getRpSmsCode/**")
                .excludePathPatterns("/v2/user/resetPswd")
                .order(0);
        registry.addInterceptor(realNameInterceptor())
                .addPathPatterns("/v2/uasset/rent", "/v2/uasset/transfer", "/v2/uasset/withdraw")
                .order(1);
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
        bean.setOrder(0);  // 跨域Filter排序，排到第一个
        return bean;
    }

    /**
     * 短信配置
     *
     * @return 短信配置类
     */
    @Bean
    public SMSProperties smsProperties() {
        return new SMSProperties();
    }

}
