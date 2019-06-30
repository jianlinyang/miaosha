package com.shu.miaosha.config;

import com.shu.miaosha.access.AccessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author yang
 * @date 2019/6/29 10:36
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    private final UserArgumentResolver userArgumentResolver;

    private final AccessInterceptor accessInterceptor;

    public WebConfig(UserArgumentResolver userArgumentResolver, AccessInterceptor accessInterceptor) {
        this.userArgumentResolver = userArgumentResolver;
        this.accessInterceptor = accessInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册
        registry.addInterceptor(accessInterceptor);
    }
}
