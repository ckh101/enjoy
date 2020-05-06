package com.hbnet.fastsh.web.service.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hbnet.fastsh.redis.consumer.BusinessType;
import com.hbnet.fastsh.redis.consumer.RedisMessage;
import com.hbnet.fastsh.redis.consumer.vo.CommonBody;
import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.dto.DailyReportItem;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.service.impl.AdvertiserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Component
@Slf4j
public class UploadConversionActionsToGdtJob implements SimpleJob {
    private int PARALLEL_SIZE = 20; // 任务并行最大数量
    private Semaphore semaphore = new Semaphore(PARALLEL_SIZE, true);
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(PARALLEL_SIZE, PARALLEL_SIZE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    @Autowired
    AdvertiserService advertiserService;

    @Autowired
    RedisService redisService;

    @Override
    public void execute(ShardingContext shardingContext){
        if(redisService.incr("UploadConversionActionsToGdtJob", 60L) > 0){
            return;
        }
        log.info("UploadConversionActionsToGdtJob:start");
        List<Advertiser> advs = advertiserService.getAllNorMalAdvertiserPageList();
        CountDownLatch latch = new CountDownLatch(advs.size());
        advs.forEach(adv->{
            semaphore.acquireUninterruptibly();
            pool.submit(()-> {
                process(adv);
                semaphore.release(); // 释放信号量
                latch.countDown();
            });
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.info("UploadConversionActionsToGdtJob:error");
            e.printStackTrace();
        }finally {
            log.info("UploadConversionActionsToGdtJob:end");
            redisService.delete("UploadConversionActionsToGdtJob");
        }
    }

    protected boolean process(Advertiser adv) {
        try {
            List<String> actions = redisService.lget(String.valueOf(adv.getAccountId()), 45);
            if (!Tools.isBlank(actions)) {
                log.info("accountId:"+adv.getAccountId());
                JSONArray actionArray = new JSONArray();
                actions.forEach(action -> {
                    String[] actioninfo = action.split("-");
                    String user_action_set_id = actioninfo[0];
                    String action_time = actioninfo[1];
                    String action_type = actioninfo[2];
                    String outer_action_id = actioninfo[3];
                    String click_id = actioninfo[4];
                    String amount = actioninfo[5];
                    JSONObject actionJson = new JSONObject();
                    actionJson.put("user_action_set_id", user_action_set_id);
                    actionJson.put("action_time", action_time);
                    actionJson.put("action_type", action_type);
                    actionJson.put("outer_action_id", outer_action_id);
                    JSONObject trace = new JSONObject();
                    trace.put("click_id", click_id);
                    actionJson.put("trace", trace);
                    JSONObject action_param = new JSONObject();
                    action_param.put("int_example", amount);
                    action_param.put("value", amount);
                    actionJson.put("action_param", action_param);
                    actionArray.add(actionJson);

                });

                JSONObject params = new JSONObject();
                params.put("account_id", adv.getAccountId());
                params.put("actions", actionArray);
                log.info(params.toJSONString());
                JSONObject result = GDTApiUtils.addUserActions(adv.getAccessToken(), params.toJSONString());
                if (result == null || result.getIntValue("code") != 0) {
                    log.error("add_user_action_error:" + result);
                } else {
                    log.info("result:"+result.toJSONString());
                    redisService.lbatchDel(String.valueOf(adv.getAccountId()), actions);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
