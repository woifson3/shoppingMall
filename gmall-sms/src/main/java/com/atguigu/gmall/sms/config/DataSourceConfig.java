package com.atguigu.gmall.sms.config;

import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

public class DataSourceConfig {

    @Bean
    @Primary
//    @ConfigurationProperties("spring.datasource")
    public DataSource hikariDataSource(@Value("${spring.datasource.url}") String url,
                                       @Value("${spring.datasource.driver-class-name}") String driverClassName,
                                       @Value("${spring.datasource.username}") String username,
                                       @Value("${spring.datasource.password}") String password) {

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(url);
        hikariDataSource.setDriverClassName(driverClassName);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        return new DataSourceProxy(hikariDataSource);
    }

    /**
     * 需要将 DataSourceProxy 设置为主数据源，否则事务无法回滚
     *
     * @param hikariDataSource The DruidDataSource
     * @return The default datasource


    /*@Bean
     @Primary
     @ConfigurationProperties("spring.datasource") public DataSource dataSource(@Value("{spring.datasource.url}") String url){
     HikariDataSource hikariDataSource =new HikariDataSource();
     hikariDataSource.setJdbcUrl(url);
     return  hikariDataSource;
     */


    //}

}
