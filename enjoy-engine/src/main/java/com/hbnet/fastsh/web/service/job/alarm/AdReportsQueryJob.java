package com.hbnet.fastsh.web.service.job.alarm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.google.common.collect.Lists;
import com.hbnet.fastsh.redis.consumer.BusinessType;
import com.hbnet.fastsh.redis.consumer.RedisMessage;
import com.hbnet.fastsh.redis.consumer.vo.CommonBody;
import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.web.dto.DailyReportItem;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.service.impl.AdvertiserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class AdReportsQueryJob implements SimpleJob {
    private static int PARALLEL_SIZE = 30; // 任务并行最大数量
    private final ExecutorService executor = Executors.newFixedThreadPool(PARALLEL_SIZE);
    private Semaphore semaphore = new Semaphore(PARALLEL_SIZE, true);

    @Autowired
    private AdvertiserService advertiserService;

    @Autowired
    private RedisService redisService;

    protected Logger getLogger() {
        return log;
    }

    @Override
    public void execute(ShardingContext shardingContext) {
        List<Advertiser> allAdvertiser = advertiserService.list();
        final AtomicInteger ai = new AtomicInteger(0);
        final CountDownLatch latch = new CountDownLatch(allAdvertiser.size());
        int skip = 0;
        for (Advertiser adver : allAdvertiser) {
            if (StringUtils.isBlank(adver.getAccessToken())) {
                skip++;
                latch.countDown();
                continue;
            }

            semaphore.acquireUninterruptibly(); // 获取执行权
            executor.execute(() -> {
                boolean ret = process(adver);
                if (ret) {
                    ai.incrementAndGet();
                }

                semaphore.release(); // 释放信号量
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException ex) {
            getLogger().error("计数失败!", ex);
        }

        getLogger().info("查询广告组日报表结束!总数={},成功={},跳过={}", allAdvertiser.size(), ai.get(), skip);
    }

    protected boolean process(Advertiser adver) {
        try {
            DateFormat fomatter = new SimpleDateFormat("yyyy-MM-dd");
            List<DailyReportItem> costData = getReportDataByDate(adver, fomatter.format(new Date()));

            if (costData.isEmpty()) {
                return true;
            }

            String bodyId = Long.toHexString(System.currentTimeMillis()) + RandomUtils.nextInt(1000, 9999);
            String body = JSON.toJSONString(costData);

            RedisMessage msg = new RedisMessage(BusinessType.DAILY_REPORT_QUERY.getValue(), new CommonBody(bodyId, body));
            redisService.publish("cn.hbnet.smartad", msg);
        } catch (Exception ex) {
            getLogger().error("查询推送日报表出错!account_id={}，错误:{}", adver.getAccountId(), ex);
            return false;
        }

        return true;
    }

    protected List<DailyReportItem> getReportDataByDate(Advertiser adver, String dateYmd) {
        Map<String, String> params = new HashMap() {{
            this.put("level", "REPORT_LEVEL_AD");
            this.put("account_id", adver.getAccountId().toString());
            this.put("group_by", "[\"ad_id\"]");
            this.put("date_range", "{\"start_date\":\"" + dateYmd + "\",\"end_date\":\"" + dateYmd + "\"}");
            this.put("fields", "[\"ad_id\",\"adgroup_id\",\"campaign_id\",\"cost\", \"web_order_roi\",\"web_order_amount\"]");
            this.put("page_size", "100");
        }};

        int page = 1;
        int totalPage = 0;

        List<DailyReportItem> campaignCostData = Lists.newArrayList();
        do {
            params.put("page", String.valueOf(page));
            JSONObject ret = GDTApiUtils.getHourlyReports(adver.getAccessToken(), params);
            if (ret.getIntValue("code") != 0) {
                getLogger().error("获取日报表失败!account_id={},page={},ret={}", adver.getAccountId(), page, ret);
                continue;
            }

            JSONObject data = ret.getJSONObject("data");
            totalPage = data.getJSONObject("page_info").getIntValue("total_page");

            List<DailyReportItem> dataList = data.getJSONArray("list").toJavaList(DailyReportItem.class);
            if (dataList != null && dataList.size() > 0) {
                dataList.stream().filter(d -> d.getCost() > 0).forEach(item -> {
                    item.setAccountId(adver.getAccountId());
                    item.setDate(dateYmd);
                    campaignCostData.add(item);
                }); // 保留当天有消耗的广告数据
            }

            page++;
        } while (page <= totalPage);

        return campaignCostData;
    }
}
