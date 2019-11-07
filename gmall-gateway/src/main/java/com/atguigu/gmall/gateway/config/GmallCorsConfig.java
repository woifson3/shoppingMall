package com.atguigu.gmall.gateway.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

;

@Configuration
public class GmallCorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter(){
        //4.根据3.需要一个configuration对象，这里new一个，并且放一些参数进去   需要什么参数，就是看源码了
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:1000");//可以请求成功的域名
        configuration.addAllowedHeader("*");   //可以请求成功的请求头
        configuration.addAllowedMethod("*");  //可以请求成功的方法
        configuration.setAllowCredentials(true);   //携带cookie

        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();//2.注意jar包的选择   UrlBasedCorsConfigurationSource是ConfigurationSource的实现类
        configurationSource.registerCorsConfiguration("/**",configuration);

        return new CorsWebFilter(configurationSource);//1.网关使用CorsWebFilter过滤
    }

}
