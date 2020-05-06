package com.hbnet.fastsh.consumer.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.mongodb.service.impl.AdDailyDataHistoryService;
import com.hbnet.fastsh.redis.consumer.BusinessType;
import com.hbnet.fastsh.redis.consumer.vo.CommonBody;
import com.hbnet.fastsh.redis.receiver.AbstractReceiver;
import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.web.dto.GdtAd;
import com.hbnet.fastsh.web.dto.DailyReportItem;
import com.hbnet.fastsh.web.entity.*;
import com.hbnet.fastsh.web.enums.ConfiguredStatusEnum;
import com.hbnet.fastsh.web.repositories.AdMonitorConfigRepository;
import com.hbnet.fastsh.web.service.alarm.AlarmService;
import com.hbnet.fastsh.web.service.http.AdApiService;
import com.hbnet.fastsh.web.service.impl.AdGroupService;
import com.hbnet.fastsh.web.service.impl.AdService;
import com.hbnet.fastsh.web.service.impl.AdvertiserService;
import com.hbnet.fastsh.web.service.impl.AlarmFlagService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.hbnet.fastsh.web.service.impl.AlarmFlagService.FLAG_ROI_THRESHOLD_ALARM;

/**
 * @ClassName: Redis消息消费者
 * @Auther: zoulr@qq.com
 * @Date: 2019/3/29 16:18
 * @Description: 目前主要监听DAILY_REPORT_QUERY通知
 */
@Slf4j
@Component
public class DailyReportMessageReceiver extends AbstractReceiver {
    private static final String KEY_ANTI_DUPLICATION_PREFIX = "smart-drq-";

    @Autowired
    private RedisService redisService;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private AdMonitorConfigRepository adMonitorConfigRepository;

    @Autowired
    private AdGroupService adGroupService;

    @Autowired
    private AdDailyDataHistoryService adDailyDataHistoryService;

    @Autowired
    private AdApiService adApiService;

    @Autowired
    private AdvertiserService advertiserService;

    @Autowired
    private AdService adService;

    @Autowired
    private AlarmFlagService alarmFlagService;

    /**
     * 处理预警
     * @param ad
     * @param dri
     */
    private void handleAlarm(Ad ad, DailyReportItem dri) {
        AdGroup adGroup = ad.getAdGroup();
        int currentCost = dri.getCost(); // 当日计划的消耗量(分)
        int dailyBudget = adGroup.getDailyBudget() == null ? 0 : adGroup.getDailyBudget(); // 日预算(分)
        AdMonitorConfig adMonitorConfig = adMonitorConfigRepository.findByAdGroupId(adGroup.getAdGroupId());
        if (adMonitorConfig == null) {
            log.warn("广告组{}缺少预警配置！", adGroup.getAdGroupId());
            return;
        }

        // 日消耗预警
        int dailyCostThreshold = adMonitorConfig.getDailyCostThreshold() == null ? 0 : adMonitorConfig.getDailyCostThreshold();
        if (dailyCostThreshold > 0 && currentCost > dailyCostThreshold) {
            alarmService.notifyCostThresholdExceed(ad, dri.getDate(), adMonitorConfig, currentCost, dailyCostThreshold);
        }

        // 日预算预警
        double dailyBudgetThreshold = adMonitorConfig.getDailyBudgetThreshold() == null ? 0 : adMonitorConfig.getDailyBudgetThreshold();
        if (dailyBudgetThreshold > 0 && dailyBudget > 0) {
            double budgetLeftPercent = 1D - (Double.valueOf(currentCost) / Double.valueOf(dailyBudget));
            if (budgetLeftPercent > dailyBudgetThreshold) {
                alarmService.notifyBudgetThresholdExceed(ad, dri.getDate(), adMonitorConfig, budgetLeftPercent, dailyBudgetThreshold);
            }
        }

        // ROI预警
        double roiThreshold = adMonitorConfig.getRoiThreshold() == null ? 0 : adMonitorConfig.getRoiThreshold();
        if (roiThreshold > 0) {
            // 用今天的数据加聚合结果计算目前ROI
            double currentRoi = adDailyDataHistoryService.calHistoryRoi(adGroup.getAdGroupId(), Double.valueOf(dri.getWebOrderAmount()), Double.valueOf(dri.getCost()));
            String flag = FLAG_ROI_THRESHOLD_ALARM + "-" + ad.getAdId();
            AlarmFlag alarmFlag = alarmFlagService.findByFlag(flag);
            if (currentRoi < roiThreshold) {
                if (alarmFlag == null) {
                    alarmService.notifyRoiThresholdExceed(ad, adMonitorConfig, currentRoi, roiThreshold);
                    alarmFlagService.createFlag(flag, String.valueOf(roiThreshold)); // 保存标识
                }

                if (adMonitorConfig.getRoiAlarmAutoPause() == 1) {
                    setAdPauseAndNotify(ad, adMonitorConfig, currentRoi, roiThreshold);
                }
            } else if (alarmFlag != null) { // 大过阈值时，清除通知flag
                alarmFlagService.delete(alarmFlag);
            }
        }
    }

    /**
     * 暂停广告，先拉取最新的广告数据，再更新状态为暂停。并同步状态到本地
     * @param ad
     */
    private void setAdPauseAndNotify(Ad ad, AdMonitorConfig adMonitorConfig, double currentRoi, double roiThreshold) {
        try {
            Advertiser advertiser = advertiserService.findByAccountId(ad.getAccountId());
            GdtAd gdtAd = adApiService.get(advertiser, ad.getAdId());

            // 正常状态时才暂停
            if (ConfiguredStatusEnum.AD_STATUS_NORMAL.getValue().equals(gdtAd.getConfiguredStatus())) {
                gdtAd.setConfiguredStatus(ConfiguredStatusEnum.AD_STATUS_SUSPEND.getValue());
                boolean ret = adApiService.update(advertiser, gdtAd);
                if (ret) {
                    alarmService.notifyRoiCauseAutoPause(ad, adMonitorConfig, currentRoi, roiThreshold);
                    adService.updateConfiguredStatus(ad.getAdId(), ConfiguredStatusEnum.AD_STATUS_SUSPEND);
                }
            }
        } catch (Exception ex) {
            log.error("暂停广告出错！accoundId={},adId={}", ad.getAccountId(), ad.getAdId(), ex);
        }
    }

    @Override
    public boolean accept(BusinessType bizType, JSONObject data) {
        return BusinessType.DAILY_REPORT_QUERY == bizType;
    }

    @Override
    public void handle(BusinessType bizType, JSONObject bodyData) {
        CommonBody body = bodyData.toJavaObject(CommonBody.class);
        if (redisService.incr(KEY_ANTI_DUPLICATION_PREFIX + bizType.getValue() + "-" + body.getBodyId(), 10L) > 0) {
            return; // 已经执行过
        };

        List<DailyReportItem> data = JSON.parseArray(body.getData(), DailyReportItem.class);
        for (DailyReportItem dri : data) {
            Ad ad = adService.findByAdId(dri.getAdId());
            if (ad == null) {
                continue;
            }

            handleAlarm(ad, dri);
        }
    }

    @Override
    public Logger getLogger() {
        return log;
    }

}