package com.ckh.enjoy.mongodb.service.base;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/*
 * 多数据源配置
 */
@Configuration
public class MultipleMongoProperties {

    @Primary
    @Bean(name="enjoyMongoProperties")
    @ConfigurationProperties(prefix="spring.data.mongodb.enjoy")
    public MongoProperties enjoyMongoProperties() {
        return new MongoProperties();
    }

}
