package com.ckh.enjoy.rocketmq.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: kyle_chan
 * @CreateDate: 2020/5/7
 */
@Data
@Accessors(chain = true)
public class TestMsg {
    /**
     * 消息id
     */
    private String msgId;
    /**
     * 消息内容
     */
    private String msgText;
}
