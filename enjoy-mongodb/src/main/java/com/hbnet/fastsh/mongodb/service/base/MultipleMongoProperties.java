package com.hbnet.fastsh.mongodb.service.base;

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
    @Bean(name="smarAdMongoProperties")
    @ConfigurationProperties(prefix="spring.data.mongodb.smartad")
    public MongoProperties smartAdMongoProperties() {
        return new MongoProperties();
    }

}
