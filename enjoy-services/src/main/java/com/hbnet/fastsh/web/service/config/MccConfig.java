package com.hbnet.fastsh.web.service.config;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MccConfig {
    @Autowired
    SockIOPoolConfig sockIOPoolConfig;
    @Primary
    @Bean(name="mccSocketIOPool")
    public SockIOPool sockIOPool(){
        //获取连接池的实例
        SockIOPool pool = SockIOPool.getInstance(sockIOPoolConfig.getPrefix());
        //服务器列表及其权重
        String[] servers = sockIOPoolConfig.getServers();
        Integer[] weights = sockIOPoolConfig.getWeights();
        //设置服务器信息
        pool.setServers(servers);
        pool.setWeights(weights);
        //设置初始连接数、最小连接数、最大连接数、最大处理时间
        pool.setInitConn(sockIOPoolConfig.getInitConn());
        pool.setMinConn(sockIOPoolConfig.getMinConn());
        pool.setMaxConn(sockIOPoolConfig.getMaxConn());
        //设置连接池守护线程的睡眠时间
        pool.setMaintSleep(sockIOPoolConfig.getMaintSleep());
        //设置TCP参数，连接超时
        pool.setNagle(sockIOPoolConfig.isNagle());
        pool.setSocketConnectTO(sockIOPoolConfig.getSocketConnectTO());
        pool.setSocketTO(sockIOPoolConfig.getSocketTO());
        pool.setFailback(sockIOPoolConfig.isFailback());
        pool.setFailover(sockIOPoolConfig.isFailover());
        //初始化并启动连接池
        pool.initialize();
        return pool;
    }
    @Primary
    @Bean(name="mcc")
    @ConditionalOnBean(SockIOPool.class)
    public MemCachedClient memCachedClient(){
        return new MemCachedClient(sockIOPoolConfig.getPrefix());
    }
}
