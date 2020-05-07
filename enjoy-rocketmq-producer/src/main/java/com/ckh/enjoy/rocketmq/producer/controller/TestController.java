package com.ckh.enjoy.rocketmq.producer.controller;

import com.ckh.enjoy.rocketmq.entity.TestMsg;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author: kyle_chan
 * @CreateDate: 2020/5/7
 */
@RestController
public class TestController {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 普通消息投递
     */
    @GetMapping("/test")
    public String test(String msg) {
        TestMsg testMessaging = new TestMsg()
                .setMsgId(UUID.randomUUID().toString())
                .setMsgText(msg);
        rocketMQTemplate.convertAndSend("add-bonus", testMessaging);
        return "投递消息 => " + msg + " => 成功";
    }
}
