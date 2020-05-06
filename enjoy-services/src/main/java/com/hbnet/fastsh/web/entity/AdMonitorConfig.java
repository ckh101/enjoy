package com.hbnet.fastsh.web.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: AdMonitorConfig
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/19 10:56
 */
@Entity
@Table(name = AdMonitorConfig.TB_NAME)
@Data
public class AdMonitorConfig extends BaseEntity {
    public static final String TB_NAME = "smartad_ad_monitor_config";
    private static final long serialVersionUID = -1715091087311731851L;

    /**
     * account_id
     */
    @Column(name = "account_id")
    private Integer accountId;

    /**
     * ad_group_id
     */
    @Column(name = "ad_group_id")
    private Integer adGroupId;

    @Column(name = "ad_id")
    private Integer adId;

    /**
     * daily_order_threshold
     */
    @Column(name = "daily_order_interval")
    private Integer dailyOrderInterval;

    /**
     * 日首单预警开关
     */
    @Column(name = "daily_first_order_alarm")
    private Integer dailyFirstOrderAlarm;

    /**
     * daily_cost_threshold
     */
    @Column(name = "daily_cost_threshold")
    private Integer dailyCostThreshold;

    /**
     * 日预算预警剩余比例
     */
    @Column(name = "daily_budget_threshold")
    private Double dailyBudgetThreshold;

    /**
     * ROI预警阈值
     */
    @Column(name = "roi_threshold")
    private Double roiThreshold;

    /**
     * ROI低于指定值自动暂停开关
     */
    @Column(name = "roi_alarm_auto_pause")
    private Integer roiAlarmAutoPause;

    @Column(name = "receive_phone")
    private String receivePhone;

}
