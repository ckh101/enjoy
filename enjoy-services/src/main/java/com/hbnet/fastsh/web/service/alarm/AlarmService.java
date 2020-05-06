package com.hbnet.fastsh.web.service.alarm;

import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.utils.AliSMSUtil;
import com.hbnet.fastsh.web.entity.Ad;
import com.hbnet.fastsh.web.entity.AdMonitorConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: AlarmService
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/19 11:15
 */
@Slf4j
@Service
public class AlarmService {
    private static final String PREFIX_ALARM_CACHE_KEY = "smartad-alarm-";
    private final ExecutorService executor = Executors.newFixedThreadPool(30);

    @Autowired
    private RedisService redisService;

    /**
     * 首单发送通知（不用加缓存标识）
     * @param adMonitorConfig
     */
    public void notifyFirstOrder(Ad ad, AdMonitorConfig adMonitorConfig) {
        String[] receivePhones = StringUtils.split(StringUtils.defaultString(adMonitorConfig.getReceivePhone()), ',');
        String adName = StringUtils.abbreviate(ad.getAdName(), 10);

        if (receivePhones.length == 0) {
            log.info("首单预警未发送,广告ID:{},缺少手机", adMonitorConfig.getAdId());
            return;
        }

        log.info("首单预警发送,广告ID:{},手机:{}", adMonitorConfig.getAdId(), adMonitorConfig.getReceivePhone());
        executor.execute(() -> {
            for (String phone : receivePhones) {
                AliSMSUtil.adFirstOrderNotify(phone, ad.getAdId(), adName);
            }
        });
    }

    /**
     * 每隔N单发送通知（不用加缓存标识）
     * @param monitorConfig
     * @param currentOrderCount
     */
    public void notifyDailyOrderInterval(Ad ad, AdMonitorConfig monitorConfig, int currentOrderCount) {
        String[] receivePhones = StringUtils.split(monitorConfig.getReceivePhone(), ',');
        if (receivePhones.length == 0) {
            log.info("间隔{}单预警未发送,广告组ID:{},广告ID:{},缺少手机",  monitorConfig.getAdGroupId(), monitorConfig.getAdId());
            return;
        }

        String adName = StringUtils.abbreviate(ad.getAdName(), 10);
        log.info("间隔{}单预警发送,广告组ID:{},广告ID:{},手机:{}", monitorConfig.getAdGroupId(), monitorConfig.getAdId(), monitorConfig.getReceivePhone());
        executor.execute(() -> {
            for (String phone : receivePhones) {
                AliSMSUtil.adOrderIntervalNotify(phone, ad.getAdId(), adName, currentOrderCount);
            }
        });
    }

    /**
     * 预算消耗超过阈值预警
     * @param dateFlag
     * @param monitorConfig
     * @param currentCost
·     * @param dailyCostThreshold
     */
    public void notifyCostThresholdExceed(Ad ad, String dateFlag, AdMonitorConfig monitorConfig, int currentCost, int dailyCostThreshold) {
        String cacheKey = PREFIX_ALARM_CACHE_KEY + "cost-notify-" + dateFlag + "-" + ad.getAdId();
        if (redisService.exist(cacheKey)) {
            return;
        }

        // 发送预警短信
        String[] receivePhones = StringUtils.split(StringUtils.defaultString(monitorConfig.getReceivePhone()), ',');
        if (receivePhones.length == 0) {
            log.info("消耗阈值预警未发送,广告ID:{},缺少手机", monitorConfig.getAdId());
            return;
        }

        String adName = StringUtils.abbreviate(ad.getAdName(), 10);
        log.info("消耗阈值预警发送,广告ID:{},{}-{},手机:{}", monitorConfig.getAdId(), currentCost, dailyCostThreshold, monitorConfig.getReceivePhone());
        executor.execute(() -> {
            for (String phone : receivePhones) {
                AliSMSUtil.adCostThresholdExceedNotify(phone, ad.getAdId(), adName, currentCost, dailyCostThreshold);
            }
        });

        redisService.set(cacheKey, 1, DateUtils.MILLIS_PER_DAY);
    }

    /**
     * 预算剩余低于阈值预警
     * @param dateFlag
     * @param monitorConfig
     * @param budgetLeftPercent
     * @param dailyBudgetThreshold
     */
    public void notifyBudgetThresholdExceed(Ad ad, String dateFlag, AdMonitorConfig monitorConfig, double budgetLeftPercent, double dailyBudgetThreshold) {
        String cacheKey = PREFIX_ALARM_CACHE_KEY + "budget-notify-" + dateFlag + "-" + ad.getAdId();
        if (redisService.exist(cacheKey)) {
            return;
        }

        // 发送预警短信
        String[] receivePhones = StringUtils.split(StringUtils.defaultString(monitorConfig.getReceivePhone()), ',');
        if (receivePhones.length == 0) {
            log.info("预算剩余预警未发送,广告ID:{},缺少手机", monitorConfig.getAdId());
            return;
        }

        String adName = StringUtils.abbreviate(ad.getAdName(), 10);
        log.info("预算剩余预警发送,广告ID:{},{}-{},手机:{}",  monitorConfig.getAdId(), budgetLeftPercent, dailyBudgetThreshold, monitorConfig.getReceivePhone());
        executor.execute(() -> {
            for (String phone : receivePhones) {
                AliSMSUtil.adBudgetThresholdExceedNotify(phone, ad.getAdId(), adName, dailyBudgetThreshold);
            }
        });

        redisService.set(cacheKey, 1, DateUtils.MILLIS_PER_DAY);
    }

    /**
     * ROI低于阈值预警
     * @param monitorConfig
     * @param currentRoi
     * @param roiThreshold
     */
    public void notifyRoiThresholdExceed(Ad ad, AdMonitorConfig monitorConfig, double currentRoi, double roiThreshold) {
        // 发送预警短信
        String[] receivePhones = StringUtils.split(StringUtils.defaultString(monitorConfig.getReceivePhone()), ',');
        if (receivePhones.length == 0) {
            log.info("ROI过低预警未发送,广告ID:{},缺少手机", monitorConfig.getAdId());
            return;
        }

        String adName = StringUtils.abbreviate(ad.getAdName(), 10);
        log.info("ROI过低预警发送,广告ID:{},{}-{},手机:{}", monitorConfig.getAdId(), currentRoi, roiThreshold, monitorConfig.getReceivePhone());
        executor.execute(() -> {
            for (String phone : receivePhones) {
                AliSMSUtil.adRoiThresholdExceedNotify(phone, ad.getAdId(), adName, currentRoi, roiThreshold);
            }
        });
    }

    /**
     * 自动暂停通知
     * @param ad
     */
    public void notifyRoiCauseAutoPause(Ad ad, AdMonitorConfig monitorConfig, double currentRoi, double roiThreshold) {
        // 发送预警短信
        String[] receivePhones = StringUtils.split(StringUtils.defaultString(monitorConfig.getReceivePhone()), ',');
        if (receivePhones.length == 0) {
            log.info("ROI自动暂停预警未发送,广告ID:{},缺少手机", monitorConfig.getAdId());
            return;
        }

        String adName = StringUtils.abbreviate(ad.getAdName(), 10);
        log.info("ROI自动暂停预警发送,广告ID:{},{}-{},手机:{}", monitorConfig.getAdId(), currentRoi, roiThreshold, monitorConfig.getReceivePhone());
        executor.execute(() -> {
            for (String phone : receivePhones) {
                AliSMSUtil.adRoiCauseAutoPauseNotify(phone, ad.getAdId(), adName, currentRoi, roiThreshold);
            }
        });
    }

    /**
     * 清除当天标识（预算消耗超过阈值预警）
     * @param date
     */
    public void clearFlagCostThresholdExceed(Date date, int adId) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String cacheKey = PREFIX_ALARM_CACHE_KEY + "cost-notify-" + dateFormat.format(date) + "-" + adId;
        redisService.delete(cacheKey);
    }

    /**
     * 清除当天标识（预算剩余低于阈值预警）
     * @param date
     */
    public void clearFlagBudgetThresholdExceed(Date date, int adId) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String cacheKey = PREFIX_ALARM_CACHE_KEY + "budget-notify-" + dateFormat.format(date) + "-" + adId;
        redisService.delete(cacheKey);
    }
}
