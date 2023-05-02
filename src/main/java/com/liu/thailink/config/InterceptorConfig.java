package com.liu.thailink.config;


import com.liu.thailink.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")  // intercept all path
                .excludePathPatterns("/user/login","/user/email-auth");
    }

    @Bean
    public HandlerInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

}