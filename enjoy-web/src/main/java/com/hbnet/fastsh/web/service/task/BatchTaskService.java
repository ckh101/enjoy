package com.hbnet.fastsh.web.service.task;

import com.alibaba.fastjson.JSON;
import com.hbnet.fastsh.constants.WebConstants;
import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.utils.SessionUtil;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.entity.Campaign;
import com.hbnet.fastsh.web.service.impl.AdService;
import com.hbnet.fastsh.web.service.impl.AdvertiserService;
import com.hbnet.fastsh.web.service.impl.CampaignService;
import com.hbnet.fastsh.web.vo.task.AdTaskParam;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @ClassName: BatchTaskService
 * @Auther: zoulr@qq.com
 * @Date: 2019/8/22 15:34
 */
@Service
public class BatchTaskService {
    private static final int PARALLEL_SIZE = 40;
    private ExecutorService executor = Executors.newFixedThreadPool(PARALLEL_SIZE / 2); // 执行线程数=最大任务数/2
    private Semaphore semaphore = new Semaphore(PARALLEL_SIZE, true);

    @Autowired
    private RedisService redisService;

    public String createCopyTask(Advertiser adv, Campaign campaign, List<Long> idList) {
        String taskId = generateTaskId("bcopy", adv.getAccountId());

        AdTaskParam p = new AdTaskParam();
        p.setTaskId(taskId);
        p.setType("adCopy");
        p.setAccountId(adv.getAccountId());
        p.setCampaignId(campaign == null ? 0 : campaign.getCampaignId());
        p.setIds(idList);
        p.setCreatorId(SessionUtil.getCurUser().getId());

        redisService.set("smartad-task-" + taskId, JSON.toJSONString(p), DateUtils.MILLIS_PER_MINUTE * 20); // 20分钟
        redisService.queuePush(WebConstants.REDIS_KEY_BATCH_TASK_QUEUE, taskId); // 任务ID入队

        return taskId;
    }

    public String createSuspendTask(Advertiser adv, List<Long> idList) {
        String taskId = generateTaskId("bsusp", adv.getAccountId());

        // TODO
        return taskId;
    }

    public String createActivateTask(Advertiser adv, List<Long> idList) {
        String taskId = generateTaskId("bact", adv.getAccountId());
        // TODO
        return taskId;
    }

    public String createDeleteTask(Advertiser adv, List<Long> idList) {
        String taskId = generateTaskId("bdel", adv.getAccountId());
        // TODO
        return taskId;
    }

    private String generateTaskId(String bizName, int accountId) {
        return bizName + "-" + accountId + Long.toHexString(System.currentTimeMillis()) + RandomStringUtils.random(4, true, true);
    }
}
