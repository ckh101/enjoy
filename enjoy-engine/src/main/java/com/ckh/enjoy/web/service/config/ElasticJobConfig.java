package com.ckh.enjoy.web.service.config;

import com.ckh.enjoy.web.service.job.SimpleJobDemo;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Configuration
@Data
@Component
@ConfigurationProperties(prefix = "elasticjob")
public class ElasticJobConfig {
    private String serverlists;
    private String namespace;
    private int baseSleepTimeMilliseconds;
    private int maxSleepTimeMilliseconds;
    private int maxRetries;

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;
    /**
     * 配置zookeeper

     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter() {
        ZookeeperConfiguration config = new ZookeeperConfiguration(serverlists, namespace);
        config.setMaxRetries(maxRetries);
        config.setBaseSleepTimeMilliseconds(baseSleepTimeMilliseconds);
        config.setMaxSleepTimeMilliseconds(maxSleepTimeMilliseconds);
        config.setSessionTimeoutMilliseconds(120000);
        return new ZookeeperRegistryCenter(config);
    }

    /**
     * 将作业运行的痕迹进行持久化到DB
     *
     * @return
     */
    @Bean
    public JobEventRdbConfiguration jobEventConfiguration() {
        return new JobEventRdbConfiguration(dataSource);
    }

    @Bean
    public ElasticJobListener elasticJobListener() {
        return new MyElasticJobListener();
    }

    /**
     * 配置任务详细信息
     * @param jobClass
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @return
     */
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
                JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount).failover(true)
                        .shardingItemParameters(shardingItemParameters).build()
                , jobClass.getCanonicalName())
        ).overwrite(true).monitorExecution(true).monitorPort(6666).reconcileIntervalMinutes(10).build();
    }


    @Bean(name="simpleJobDemoJob",initMethod = "init")
    @Primary
    public JobScheduler simpleJobDemoJob(final SimpleJobDemo simpleJobDemo, @Value("*/1 * * * * ?") final String cron) {

        return new SpringJobScheduler(simpleJobDemo, regCenter(),
                getLiteJobConfiguration(simpleJobDemo.getClass(), cron, 1, "0=A1"),
                jobEventConfiguration(), elasticJobListener());
    }





}
