package com.ckh.enjoy.web.config;

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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryEnjoy",
        transactionManagerRef = "transactionManagerEnjoy",
        basePackages = {"com.ckh.enjoy.web.dao"})
public class EnjoyConfig {
    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.enjoy")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Resource
    private Properties jpaProperties;

    /**
     * 设置实体类所在位置
     */
    @Primary
    @Bean(name = "entityManagerFactoryEnjoy")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryEnjoy(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(dataSource())
                .packages("com.ckh.enjoy.web.entity")
                .persistenceUnit("enjoyPersistenceUnit")
                .build();
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }

    @Primary
    @Bean(name = "transactionManagerEnjoy")
    public PlatformTransactionManager transactionManagerEnjoy(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryEnjoy(builder).getObject());
    }

    /*@Bean(name = "txManager")
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManager() throws Exception {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
        return dataSourceTransactionManager;
    }*/

}
