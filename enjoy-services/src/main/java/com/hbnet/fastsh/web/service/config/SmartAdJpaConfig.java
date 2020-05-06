package com.hbnet.fastsh.web.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactorySmartAd",
        transactionManagerRef = "transactionManagerSmartAd",
        basePackages = {"com.hbnet.fastsh.web.repositories"})
public class SmartAdJpaConfig {

    @Resource
    private Properties jpaProperties;

    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.smartad")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 设置实体类所在位置
     */
    @Primary
    @Bean(name = "entityManagerFactorySmartAd")
    public LocalContainerEntityManagerFactoryBean entityManagerFactorySmartAd(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(dataSource())
                .packages("com.hbnet.fastsh.web.entity")
                .persistenceUnit("enjoyPersistenceUnit")
                .build();
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }

    @Primary
    @Bean(name = "transactionManagerSmartAd")
    public PlatformTransactionManager transactionManagerSmartAd(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactorySmartAd(builder).getObject());
    }
}
