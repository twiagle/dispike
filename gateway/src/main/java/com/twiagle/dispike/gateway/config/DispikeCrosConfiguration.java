package com.twiagle.dispike.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class DispikeCrosConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //1、配置跨域
        corsConfiguration.addAllowedHeader("*");//允许自定义header
        corsConfiguration.addAllowedMethod("*");//允许的http请求方法
        corsConfiguration.addAllowedOrigin("*");//允许的源
        corsConfiguration.setAllowCredentials(true);//cookie

        source.registerCorsConfiguration("/**",corsConfiguration);//任意路径都需要跨域
        return new CorsWebFilter(source);
    }
}
