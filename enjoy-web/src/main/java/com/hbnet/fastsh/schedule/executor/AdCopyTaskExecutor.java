package com.hbnet.fastsh.schedule.executor;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.utils.*;
import com.hbnet.fastsh.web.entity.*;
import com.hbnet.fastsh.web.enums.AdSystemStatusEnum;
import com.hbnet.fastsh.web.service.impl.*;
import com.hbnet.fastsh.web.vo.task.AdTaskParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: AdCopyTaskExecutor
 * @Auther: zoulr@qq.com
 * @Date: 2019/8/23 12:13
 * @Description: 广告复制执行器
 */
@Slf4j
@Component("adCopyTaskExecutor")
public class AdCopyTaskExecutor implements TaskExecutor {
    private static final int PARALLEL_SIZE = 20;

    private ExecutorService executor = Executors.newFixedThreadPool(PARALLEL_SIZE / 2); // 执行线程数=最大任务数/2
    private Semaphore semaphore = new Semaphore(PARALLEL_SIZE, true);

    @Autowired
    private AdService adService;

    @Autowired
    private AdvertiserService advertiserService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private AdGroupService adGroupService;

    @Autowired
    private AdCreativeService adCreativeService;

    @Autowired
    private RedisService redisService;

    @Override
    public void submit(AdTaskParam taskParam) {
        final AtomicInteger ai = new AtomicInteger(0);
        final CountDownLatch latch = new CountDownLatch(taskParam.getIds().size());
        long start = System.currentTimeMillis();

        final Advertiser advertiser = advertiserService.findByAccountId(taskParam.getAccountId());
        final Campaign campaign;
        if (taskParam.getCampaignId() > 0) {
            campaign = campaignService.findByCampaignId(taskParam.getCampaignId());
        } else {
            campaign = null;
        }

        List<Ad> adList = adService.findByIds(taskParam.getIds());
        Map<Long, JSONObject> message = Maps.newConcurrentMap();

        for (Ad ad : adList) {
            semaphore.acquireUninterruptibly(); // 获取执行权
            executor.execute(() -> {
                boolean ret = copy(advertiser, campaign, ad, message);
                semaphore.release(); // 释放信号量
                latch.countDown();
                if (ret) {
                    ai.incrementAndGet();
                    log.info("复制广告成功:tid={},adId={}", taskParam.getTaskId(), ad.getAdId());
                } else {
                    log.warn("复制广告失败!tid={},adId={}", taskParam.getTaskId(), ad.getAdId());
                }
            });
        }

        try {
            latch.await();

            // 按照用户提交顺序返回
            List<JSONObject> messageList = Lists.newArrayListWithExpectedSize(message.size());
            for (Long id : taskParam.getIds()) {
                messageList.add(message.get(id));
            }

            JSONObject info = new JSONObject();
            info.put("total", adList.size());
            info.put("succedCnt", ai.get());
            info.put("msgList", messageList);
            info.put("taskType", taskParam.getType());
            info.put("creatorId", taskParam.getCreatorId());

            redisService.set("smartad-task-" + taskParam.getTaskId(), info.toJSONString(), DateUtils.MILLIS_PER_MINUTE * 10);
        } catch (InterruptedException ex) {
            log.error("计数失败!tid={}", taskParam.getTaskId(), ex);
        }

        log.info("复制广告统计!tid={},num={},suc={},use={}", taskParam.getTaskId(), adList.size(), ai.get(), System.currentTimeMillis() - start);
    }

    public boolean copy(Advertiser adv, Campaign campaign, Ad ad, Map<Long, JSONObject> messageContainer) {
        String date = Tools.format(new Date(), "yyyy-MM-dd");
        if (!"1970-01-01".equals(ad.getAdGroup().getEndDate()) && date.compareTo(ad.getAdGroup().getEndDate()) > 0) {
            messageContainer.put(ad.getId(), buildMessage(ad, false, "广告已过期，无法复制！"));
            return false;
        }

        if (date.compareTo(ad.getAdGroup().getBeginDate()) > 0) {
            ad.getAdGroup().setBeginDate(date);
        }

        if ("1970-01-01".equals(ad.getAdGroup().getEndDate())) {
            ad.getAdGroup().setEndDate("");
        }

        try {
            AdCreative adCreative = ad.getAdCreative();
            JSONObject adCreativeElements = JSONObject.parseObject(adCreative.getAdCreativeElements());
            byte[] fileBytes = OkHttpUtils.getInstance().getRemoteImage(adCreative.getMaterialUrls());
            String signature = Tools.md5(fileBytes);
            JSONObject image = GDTApiUtils.addImages(adv.getAccessToken(), String.valueOf(adv.getAccountId()), fileBytes, signature);

            if (image == null || image.getIntValue("code") != 0) {
                messageContainer.put(ad.getId(), buildMessage(ad, false, "上传素材出错！"));
                return false;
            }

            String imageId = image.getJSONObject("data").getString("image_id");
            if("CAMPAIGN_TYPE_WECHAT_MOMENTS".equals(ad.getCampaign().getCampaignType())){
                adCreativeElements.put("image_list", "["+imageId+"]");
            }else{
                adCreativeElements.put("image", imageId);
            }
            Ad newAd = null;
            if (campaign != null) {
                newAd = copyAd(ad, false);
                newAd.setCampaign(campaign);
            } else {
                newAd = copyAd(ad, true);
            }

            newAd.getAdCreative().setAdCreativeElements(adCreativeElements.toJSONString());
            newAd.getAdGroup().setBeginDate(date);
            newAd.setAccountId(adv.getAccountId());
            newAd.getAdGroup().setAccountId(adv.getAccountId());
            newAd.getAdGroup().setUserActionSets("[{\"type\":\"USER_ACTION_SET_TYPE_WEB\",\"id\":" + adv.getWebUserActionSetId() + "}]");
            newAd.getAdGroup().getTargeting().setCustomAudience(null);
            newAd.getAdGroup().getTargeting().setExcludedCustomAudience(null);
            newAd.getCampaign().setAccountId(adv.getAccountId());
            newAd.getAdCreative().setAccountId(adv.getAccountId());
            if (!addAd(newAd, adv)) {
                messageContainer.put(ad.getId(), buildMessage(ad, false, "复制失败，数据异常！"));
                return false;
            }
        } catch (Exception ex) {
            messageContainer.put(ad.getId(), buildMessage(ad, false, "系统繁忙，请稍后重试！"));

            log.error("复制广告出错,广告主ID:{},广告ID:{},计划ID:{}!", adv.getAccountId(), ad.getAdId(), (campaign == null ? 0 : campaign.getCampaignId()), ex);
            return false;
        }

        messageContainer.put(ad.getId(), buildMessage(ad, true, "复制成功！"));

        return true;
    }

    private JSONObject buildMessage(Ad ad, boolean copySucceed, String extraMessage) {
        JSONObject json = new JSONObject();
        json.put("id", ad.getAdId());
        json.put("adGroupId", ad.getAdGroup().getAdGroupId());
        json.put("name", ad.getAdName());
        json.put("succeed", copySucceed);
        json.put("msg", extraMessage);

        return json;
    }

    protected boolean addAd(Ad ad, Advertiser adv) {
        Campaign campaign;
        if (ad.getCampaign().getId() == null) {
            campaign = campaignService.saveOrUpdate(ad.getCampaign());
            JSONObject campaignResult = campaignService.addCampaignToGDT(campaign, adv);
        } else {
            campaign = ad.getCampaign();
        }

        if (campaign.getCampaignId() == null || campaign.getCampaignId() <= 0) {
            return false;
        }

        ad.getAdGroup().setCampaignId(campaign.getCampaignId());
        ad.getAdGroup().setPrivateCampaignId(campaign.getId());
        AdGroup adGroup = adGroupService.saveOrUpdate(ad.getAdGroup());
        JSONObject adGroupResult = adGroupService.addAdGroupToGDT(adGroup, adv);
        if (adGroup.getAdGroupId() == null || adGroup.getAdGroupId() <= 0) {
            return false;
        }
        AdCreative adCreative = ad.getAdCreative();
        if(adCreative.getPageType().equals("PAGE_TYPE_DEFAULT")){
            JSONObject adCreativeElements = JSONObject.parseObject(adCreative.getAdCreativeElements());
            if(adCreativeElements.containsKey("mini_program_path")){
                String path = adCreativeElements.getString("mini_program_path");
                String mini_program_id = adCreativeElements.getString("mini_program_id");
                path = path.substring(0, path.indexOf("&from=gdt"))+"&from=gdt&account_id="+adv.getAccountId()+"&user_action_set_id="+adv.getWebUserActionSetId()+"&adGroupId="+adGroup.getId();
                adCreative.setPageSpec("{\"mini_program_spec\":{\"mini_program_id\":\""+mini_program_id+"\",\"mini_program_path\":\""+path+"\"}}");
                adCreative.setPageType("PAGE_TYPE_MINI_PROGRAM_WECHAT");
                adCreativeElements.remove("mini_program_path");
                adCreativeElements.remove("mini_program_id");
                adCreative.setAdCreativeElements(adCreativeElements.toJSONString());
            }
        }else if(adCreative.getPageType().equals("PAGE_TYPE_MINI_PROGRAM_WECHAT")){
            JSONObject pageSpec = JSONObject.parseObject(adCreative.getPageSpec());
            String path = pageSpec.getJSONObject("mini_program_spec").getString("mini_program_path");
            path = path.substring(0, path.indexOf("&from=gdt"))+"&from=gdt&account_id="+adv.getAccountId()+"&user_action_set_id="+adv.getWebUserActionSetId()+"&adGroupId="+adGroup.getId();
            pageSpec.getJSONObject("mini_program_spec").put("mini_program_path", path);
            adCreative.setPageSpec(pageSpec.toJSONString());
        }
        adCreative.setCampaignId(campaign.getCampaignId());
        adCreative.setPrivateCampaignId(campaign.getId());
        adCreative = adCreativeService.saveOrUpdate(adCreative);
        JSONObject adCreativeResult = adCreativeService.addAdCreativeToGDT(adCreative, adv);

        if (adCreative.getAdCreativeId() == null || adCreative.getAdCreativeId() <= 0) {
            return false;
        }

        ad.setAdName(adGroup.getAdGroupName());
        ad.setCampaign(campaign);
        ad.setAdGroup(adGroup);
        ad.setAdCreative(adCreative);
        ad.setCreateUserId(SessionUtil.getCurUser().getId());
        JSONObject adResult = adService.addAdToGDT(ad, adv);

        if (ad.getAdId() == null || ad.getAdId() <= 0) {
            return false;
        }

        adService.saveOrUpdate(ad);

        return true;
    }

    protected Ad copyAd(Ad ad, boolean newCampaign) {
        Date now = new Date();

        Ad newAd = new Ad();
        BeanUtils.copyProperties(ad, newAd);
        newAd.setSystemStatus(AdSystemStatusEnum.AD_STATUS_PENDING.getValue());
        newAd.setRejectMessage("");
        AdGroup adGroup = new AdGroup();
        BeanUtils.copyProperties(ad.getAdGroup(), adGroup);
        AdGroupTargeting targeting = new AdGroupTargeting();
        BeanUtils.copyProperties(ad.getAdGroup().getTargeting(), targeting);
        targeting.setAdGroup(null);
        targeting.setId(null);
        targeting.setCreateTime(now);
        targeting.setUpdateTime(now);
        adGroup.setTargetingId(null);
        adGroup.setTargeting(targeting);
        adGroup.setPrivateCampaignId(null);
        adGroup.setCampaignId(null);
        adGroup.setId(null);
        adGroup.setAdGroupId(null);
        adGroup.setCreateTime(now);
        adGroup.setUpdateTime(now);
        adGroup.setIsDeleted(false);
        AdCreative adCreative = new AdCreative();
        BeanUtils.copyProperties(ad.getAdCreative(), adCreative);
        adCreative.setId(null);
        adCreative.setAdCreativeId(null);
        adCreative.setCreateTime(now);
        adCreative.setUpdateTime(now);
        adCreative.setCampaignId(null);
        adCreative.setPrivateCampaignId(null);
        adCreative.setIsDeleted(false);

        if (newCampaign) {
            Campaign campaign = new Campaign();
            BeanUtils.copyProperties(ad.getCampaign(), campaign);
            campaign.setId(null);
            campaign.setCampaignId(null);
            campaign.setCreateTime(now);
            campaign.setUpdateTime(now);
            campaign.setIsDeleted(false);
            newAd.setCampaign(campaign);
        }

        newAd.setId(null);
        newAd.setAdId(null);
        newAd.setUpdateTime(now);
        newAd.setCreateTime(now);

        newAd.setAdCreative(adCreative);
        newAd.setAdGroup(adGroup);
        newAd.setIsDeleted(false);
        return newAd;
    }
}
