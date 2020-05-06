package com.hbnet.fastsh.redis.receiver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.redis.consumer.BusinessType;
import org.slf4j.Logger;

/**
 * @ClassName: AbstractReceiver
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/24 18:02
 */
public abstract class AbstractReceiver {
    public void handleMessage(String body) {
        Object bodyObject = JSON.parse(body);
        JSONObject bodyJson = JSON.parseObject(bodyObject.toString()); // {data:{},business:""}

        String business = bodyJson.getString("business");
        BusinessType bizType = BusinessType.get(business);
        JSONObject data = bodyJson.getJSONObject("data");

        if (accept(bizType, data)) {
            getLogger().info("收到消息business={},body={}", business, body);
            handle(bizType, data);
        } else {
            getLogger().info("收到消息business={},已忽略", business);
        }
    }

    public abstract boolean accept(BusinessType bizType, JSONObject data);
    public abstract void handle(BusinessType bizType, JSONObject bodyData);
    public abstract Logger getLogger();

}
