package com.hbnet.fastsh.web.service.config;

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
import com.hbnet.fastsh.web.service.job.*;
import com.hbnet.fastsh.web.service.job.alarm.AdReportsQueryJob;
import com.hbnet.fastsh.web.service.job.alarm.YesterdayAdReportsQueryJob;
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


    @Bean(name="refreshTokenJobScheduler",initMethod = "init")
    @Primary
    public JobScheduler refreshTokenJobScheduler(final RefreshTokenJob refreshTokenJob, @Value("0 0 22 * * ?") final String cron) {

        return new SpringJobScheduler(refreshTokenJob, regCenter(),
                getLiteJobConfiguration(refreshTokenJob.getClass(), cron, 2, "0=A1,1=B1"),
                jobEventConfiguration(), elasticJobListener());
    }

    @Bean(name="refreshAgentTokenJobScheduler",initMethod = "init")
    @Primary
    public JobScheduler refreshAgentTokenJobScheduler(final RefreshAgentTokenJob syncAgentToken, @Value("0 0 10 * * ?") final String cron) {

        return new SpringJobScheduler(syncAgentToken, regCenter(),
                getLiteJobConfiguration(syncAgentToken.getClass(), cron, 1, "0=A2"),
                jobEventConfiguration(), elasticJobListener());
    }



    @Bean(name="uploadConversionActionsToGdtJobScheduler", initMethod = "init")
    @Primary
    public JobScheduler uploadConversionActionsToGdtJob(final UploadConversionActionsToGdtJob uploadConversionActionsToGdtJob, @Value("*/20 * * * * ?") final String cron){
        return new SpringJobScheduler(uploadConversionActionsToGdtJob, regCenter(),
                getLiteJobConfiguration(uploadConversionActionsToGdtJob.getClass(), cron, 2, "0=A3,1=B3"),
                jobEventConfiguration(), elasticJobListener());
    }

    /**
     * 广告前一天数据拉取 每天00:15执行
     * @param yesterdayAdReportsQueryJob
     * @param cron
     * @return
     */
    /*@Bean(name="yesterdayAdReportsQueryJobScheduler", initMethod = "init")
    @Primary
    public JobScheduler yesterdayAdReportsQueryJob(final YesterdayAdReportsQueryJob yesterdayAdReportsQueryJob, @Value("0 15 0 * * ?") final String cron){
        return new SpringJobScheduler(yesterdayAdReportsQueryJob, regCenter(),
                getLiteJobConfiguration(yesterdayAdReportsQueryJob.getClass(), cron, 1, "0=ABCD"),
                jobEventConfiguration(), elasticJobListener());
    }*/

    /**
     * 广告数据拉取 半个小时一次
     * @param adReportsQueryJob
     * @param cron
     * @return
     */
    /*@Bean(name="adReportsQueryJobScheduler", initMethod = "init")
    @Primary
    public JobScheduler adReportsQueryJob(final AdReportsQueryJob adReportsQueryJob, @Value("0 0/30 * * * ?") final String cron){
        return new SpringJobScheduler(adReportsQueryJob, regCenter(),
                getLiteJobConfiguration(adReportsQueryJob.getClass(), cron, 1, "0=A4"),
                jobEventConfiguration(), elasticJobListener());
    }*/
    /**
     * 同步广告状态信息 10分钟一次
     * @param syncAdJob
     * @param cron
     * @return
     */
    /*@Bean(name="syncAdJobScheduler",initMethod = "init")
    @Primary
    public JobScheduler syncAdJobScheduler(final SyncAdJob syncAdJob, @Value("0 0/10 * * * ?") final String cron) {

        return new SpringJobScheduler(syncAdJob, regCenter(),
                getLiteJobConfiguration(syncAdJob.getClass(), cron, 2, "0=A5,1=B5"),
                jobEventConfiguration(), elasticJobListener());
    }*/

    /**
     * 同步广告计划信息 10分钟一次
     * @param syncCampaignJob
     * @param cron
     * @return
     */
    /*@Bean(name="syncCampaignJobScheduler",initMethod = "init")
    @Primary
    public JobScheduler syncCampaignJobScheduler(final SyncCampaignJob syncCampaignJob, @Value("0 0/10 * * * ?") final String cron) {

        return new SpringJobScheduler(syncCampaignJob, regCenter(),
                getLiteJobConfiguration(syncCampaignJob.getClass(), cron, 2, "0=A6,1=B6"),
                jobEventConfiguration(), elasticJobListener());
    }*/

    /**
     * 枫叶订单数据拉取 每天01:00执行
     * @param downLoadGdtOrderJob
     * @param cron
     * @return
     */
    @Bean(name="downLoadGdtOrderJobScheduler", initMethod = "init")
    @Primary
    public JobScheduler downLoadGdtOrderJob(final DownLoadGdtOrderJob downLoadGdtOrderJob, @Value("0 0 1 * * ?") final String cron){
        return new SpringJobScheduler(downLoadGdtOrderJob, regCenter(),
                getLiteJobConfiguration(downLoadGdtOrderJob.getClass(), cron, 1, "0=A7"),
                jobEventConfiguration(), elasticJobListener());
    }
}
