package com.hbnet.fastsh.web.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName: DailyReportItem
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/18 17:08
 */
@Data
public class DailyReportItem {
    @JSONField(name = "account_id")
    private int accountId;

    @JSONField(name = "campaign_id")
    private int campaignId;

    @JSONField(name = "adgroup_id")
    private int adGroupId;

    @JSONField(name = "ad_id")
    private int adId;

    @JSONField(name = "cost")
    private int cost;

    @JSONField(name = "web_order_roi")
    private int webOrderRoi;

    @JSONField(name = "web_order_amount")
    private int webOrderAmount;

    @JSONField(name = "date")
    private String date;
}
