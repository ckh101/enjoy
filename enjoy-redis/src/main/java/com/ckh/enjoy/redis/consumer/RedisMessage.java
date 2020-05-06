package com.ckh.enjoy.redis.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: RedisMessage
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/16 15:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisMessage implements Serializable {
	private static final long serialVersionUID = -4370562146357248706L;

	private String business;
	private Object data;
}