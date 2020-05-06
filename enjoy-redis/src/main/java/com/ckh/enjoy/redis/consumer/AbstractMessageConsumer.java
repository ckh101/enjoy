package com.ckh.enjoy.redis.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @ClassName: AbstractMessageConsumer
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/16 15:41
 */
public abstract class AbstractMessageConsumer implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {
        String channel = new String(message.getChannel(), UTF_8);
        String body = new String(message.getBody(), UTF_8);

        Object bodyObject = JSON.parse(body);
        JSONObject bodyJson = JSON.parseObject(bodyObject.toString()); // {data:{},business:""}

        String business = bodyJson.getString("business");
        BusinessType bizType = BusinessType.get(business);
        JSONObject data = bodyJson.getJSONObject("data");

        if (accept(channel, bizType, data)) {
            getLogger().info("收到消息channel={},business={},body={}", channel, business, body);
            handle(channel, bizType, data);
        } else {
            getLogger().info("收到消息channel={},business={},已忽略", channel, business);
        }
    }

    public abstract boolean accept(String channel, BusinessType bizType, JSONObject data);
    public abstract void handle(String channel, BusinessType bizType, JSONObject bodyData);
    public abstract Logger getLogger();
}
