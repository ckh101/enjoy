package com.hbnet.fastsh.consumer.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.*;

/**
 * @ClassName: RedisConsumerConfiguration
 * @Auther: zoulr@qq.com
 * @Date: 2019/3/29 18:11
 * @Description: Redis消费者配置类
 */
@Configuration
public class RedisConsumerConfiguration {
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(StringRedisTemplate redisTemplate,
                                                                       MessageListener completeOrderMessageListener,
                                                                       MessageListener dailyReportMessageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());

        ChannelTopic topic = new ChannelTopic("cn.hbnet.smartad"); // 设置监听方式和频道

        Map<MessageListener, Collection<? extends Topic>> listeners = new HashMap<>();

        List<ChannelTopic> topics = Arrays.asList(topic);
        listeners.put(completeOrderMessageListener, topics);
        listeners.put(dailyReportMessageListener, topics);

        container.setMessageListeners(listeners);

        return container;
    }

    @Bean("completeOrderMessageListener")
    public MessageListener completeOrderMessageListener(CompleteOrderMessageReceiver completeOrderMessageReceiver) {
        return new MessageListenerAdapter(completeOrderMessageReceiver, "handleMessage");
    }

    @Bean("dailyReportMessageListener")
    public MessageListener dailyReportMessageListener(DailyReportMessageReceiver dailyReportMessageReceiver) {
        return new MessageListenerAdapter(dailyReportMessageReceiver, "handleMessage");
    }

}
