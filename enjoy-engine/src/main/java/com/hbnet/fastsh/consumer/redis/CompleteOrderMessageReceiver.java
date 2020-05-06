package com.hbnet.fastsh.consumer.redis;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.redis.consumer.BusinessType;
import com.hbnet.fastsh.redis.consumer.vo.CompleteOrderBody;
import com.hbnet.fastsh.redis.receiver.AbstractReceiver;
import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.web.entity.Ad;
import com.hbnet.fastsh.web.entity.AdGroup;
import com.hbnet.fastsh.web.entity.AdMonitorConfig;
import com.hbnet.fastsh.web.service.alarm.AlarmService;
import com.hbnet.fastsh.web.service.impl.AdGroupService;
import com.hbnet.fastsh.web.service.impl.AdMonitorConfigService;
import com.hbnet.fastsh.web.service.impl.AdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName: Redis消息消费者
 * @Auther: zoulr@qq.com
 * @Date: 2019/3/29 16:18
 * @Description: 目前主要监听CompleteOrder通知
 */
@Slf4j
@Component
public class CompleteOrderMessageReceiver extends AbstractReceiver {
    private static final String KEY_ANTI_DUPLICATION_PREFIX = "smart-corc-";

    @Autowired
    private RedisService redisService;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private AdMonitorConfigService adMonitorConfigService;

    @Autowired
    private AdService adService;

    @Autowired
    private AdGroupService adGroupService;

    @Override
    public boolean accept(BusinessType bizType, JSONObject data) {
        return BusinessType.COMPLETE_ORDER == bizType;
    }

    @Override
    public void handle(BusinessType bizType, JSONObject bodyData) {
        CompleteOrderBody body = bodyData.toJavaObject(CompleteOrderBody.class);
        if (redisService.incr(KEY_ANTI_DUPLICATION_PREFIX + bizType.getValue() + "-" + body.getClickId(), 10L) > 0) {
            return; // 已经执行过
        };

        Date now = new Date();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(now);
        String key = "cpl-order-" + dateStr + "-" + body.getAdGroupId();

        long count = redisService.lset(key, body.getClickId());
        if (count <= 1) {
            Date todayEnd = DateUtils.ceiling(new Date(), Calendar.DATE); // 第二天0点
            redisService.expireAt(key, todayEnd);
        }

        long adGroupKey = NumberUtils.toLong(body.getAdGroupId()); // 这个是AdGroup表的主键ID，不是adGroupId
        int adGroupId = findAdGroupIdByAdGroupKey(adGroupKey);
        AdMonitorConfig monitorConfig = adMonitorConfigService.findByGroupId(adGroupId);
        if (monitorConfig == null) {
            log.warn("广告组id={}缺少预警配置!", adGroupKey);
            return;
        }

        Ad ad = adService.findByAdId(monitorConfig.getAdId());
        // 触发出单预警
        if (count == 1 && monitorConfig.getDailyFirstOrderAlarm().equals(1)) {
            alarmService.notifyFirstOrder(ad, monitorConfig);
        }

        int threshold = monitorConfig.getDailyOrderInterval();
        if (count > 0 && threshold > 0 && count % threshold == 0) { // 每N单预警
            alarmService.notifyDailyOrderInterval(ad, monitorConfig, (int) count);
        }
    }

    private int findAdGroupIdByAdGroupKey(long adGroupKey) {
        String cacheKey = "cpl-cache-agk-" + adGroupKey;
        String cache = redisService.get(cacheKey);
        if (StringUtils.isNotBlank(cache)) {
            return NumberUtils.toInt(cache);
        }

        AdGroup group = adGroupService.findById(adGroupKey);
        if (group == null) {
            return 0;
        }

        redisService.set(cacheKey, group.getAdGroupId(), DateUtils.MILLIS_PER_DAY);
        return group.getAdGroupId();
    }

    @Override
    public Logger getLogger() {
        return log;
    }

}
