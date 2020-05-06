package com.hbnet.fastsh.mongodb.model.alarm;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = AdDailyDataHistory.CLT_NAME)
public class AdDailyDataHistory {
    @Transient
    public static final String CLT_NAME = "smartad_ad_daily_report_history";

    @Id
    @Field("_id")
    private String id;

    @Field("account_id")
    private Integer accountId;

    @Field("date")
    private String date;

    @Field("campaign_id")
    private Integer campaignId;

    @Field("adgroup_id")
    private Integer adGroupId;

    @Field("ad_id")
    private Integer adId;

    @Field("cost")
    private int cost;

    @Field("web_order_roi")
    private int webOrderRoi;

    @Field("web_order_amount")
    private int webOrderAmount;

    @Field("create_time")
    private Date createTime;
}
