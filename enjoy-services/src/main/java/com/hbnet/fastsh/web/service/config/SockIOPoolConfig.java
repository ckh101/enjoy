package com.hbnet.fastsh.web.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.memcached")
public class SockIOPoolConfig {
	/** 服务地址 */
	private String[] servers;		
	/** 权重 */
    private Integer[] weights;
    /** 初始化连接数 */
    private int initConn;
    /** 最小连接数 */
    private int minConn;
    /** 最大连接数 */
    private int maxConn;
    /** 睡眠时长 */
    private long maintSleep;
 
    private boolean nagle;
 
    private int socketTO;
    
    private int socketConnectTO;
    
    private boolean failover;
    
    private boolean failback;
    
    private String prefix;
 
	public String[] getServers() {
		return servers;
	}
 
	public void setServers(String[] servers) {
		this.servers = servers;
	}
 
	public Integer[] getWeights() {
		return weights;
	}
 
	public void setWeights(Integer[] weights) {
		this.weights = weights;
	}
 
	public int getInitConn() {
		return initConn;
	}
 
	public void setInitConn(int initConn) {
		this.initConn = initConn;
	}
 
	public int getMinConn() {
		return minConn;
	}
 
	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}
 
	public int getMaxConn() {
		return maxConn;
	}
 
	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}
 
	public long getMaintSleep() {
		return maintSleep;
	}
 
	public void setMaintSleep(long maintSleep) {
		this.maintSleep = maintSleep;
	}
 
	public boolean isNagle() {
		return nagle;
	}
 
	public void setNagle(boolean nagle) {
		this.nagle = nagle;
	}
 
	public int getSocketTO() {
		return socketTO;
	}
 
	public void setSocketTO(int socketTO) {
		this.socketTO = socketTO;
	}

	public int getSocketConnectTO() {
		return socketConnectTO;
	}

	public void setSocketConnectTO(int socketConnectTO) {
		this.socketConnectTO = socketConnectTO;
	}

	public boolean isFailover() {
		return failover;
	}

	public void setFailover(boolean failover) {
		this.failover = failover;
	}

	public boolean isFailback() {
		return failback;
	}

	public void setFailback(boolean failback) {
		this.failback = failback;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	
}

