package com.hbnet.fastsh.web.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName: GdtAd
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/24 14:09
 * @Description: 广告接口数据包装类，接口见https://developers.e.qq.com/docs/apilist/ads/ad?version=1.1#a3
 */
@Data
public class GdtAd {
    /**
     * 广告 id
     */
    @JSONField(name = "ad_id")
    private int adId;

    /**
     * 广告组 id
     */
    @JSONField(name = "adgroup_id")
    private int adgroupId;

    /**
     * 广告名称，同一帐号下的广告名称不允许重复
     * <br/>字段长度最小 1 字节，长度最大 120 字节
     */
    @JSONField(name = "ad_name")
    private String adName;

    /**
     * 客户设置的状态
     * <br/>枚举列表：{ AD_STATUS_NORMAL, AD_STATUS_SUSPEND }
     */
    @JSONField(name = "configured_status")
    private String configuredStatus;

    /**
     * 曝光监控地址，监控地址主域只允许白名单内的域名
     * <br/>字段长度最小0字节，长度最大1023字节
     */
    @JSONField(name = "impression_tracking_url")
    private String impressionTrackingUrl;

    /**
     * 点击监控地址，监控地址主域只允许白名单内的域名
     * <br/>字段长度最小 0 字节，长度最大 1024 字节
     */
    @JSONField(name = "click_tracking_url")
    private String clickTrackingUrl;

    /**
     * 是否支持赞转评
     */
    @JSONField(name = "feeds_interaction_enabled")
    private boolean feedsInteractionEnabled;
}
