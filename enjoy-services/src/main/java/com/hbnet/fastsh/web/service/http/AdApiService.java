package com.hbnet.fastsh.web.service.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.web.dto.GdtAd;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.service.impl.AdvertiserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AdApiService
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/24 14:06
 * @Description: 广告API Service
 */
@Slf4j
@Service
public class AdApiService {
    @Autowired
    private AdvertiserService advertiserService;

    public GdtAd get(int accountId, int adId) {
        Advertiser advertiser = advertiserService.findByAccountId(accountId);
        return get(advertiser, adId);
    }

    /**
     * 查询广告数据
     * @param advertiser
     * @param adId
     * @return
     */
    public GdtAd get(Advertiser advertiser, int adId) {
        Map<String, String> params = new HashMap() {{
            this.put("level", "REPORT_LEVEL_ADGROUP");
            this.put("account_id", advertiser.getAccountId().toString());
            this.put("filtering", "[{\"field\":\"ad_id\",\"operator\":\"EQUALS\",\"values\":[\"" + adId + "\"]}]");
            this.put("fields", "[\"ad_id\",\"adgroup_id\",\"ad_name\",\"configured_status\",\"impression_tracking_url\",\"click_tracking_url\",\"feeds_interaction_enabled\"]");
            this.put("page", 1);
            this.put("page_size", "1");
        }};

        JSONObject ret = GDTApiUtils.getAds(advertiser.getAccessToken(), params);
        if (ret == null || ret.getIntValue("code") != 0) {
            log.warn("查询广告失败！adId:{},ret={}", adId, ret);
        }

        JSONArray list = ret.getJSONObject("data").getJSONArray("list");
        if (list == null || list.size() == 0) {
            return null;
        }

        return list.getJSONObject(0).toJavaObject(GdtAd.class);
    }

    public boolean update(int accountId, GdtAd ad) {
        Advertiser advertiser = advertiserService.findByAccountId(accountId);
        return update(advertiser, ad);
    }

    /**
     * 更新广告
     * @param advertiser
     * @param ad
     * @return
     */
    public boolean update(Advertiser advertiser, GdtAd ad) {
        JSONObject json = (JSONObject) JSON.toJSON(ad);
        Map<String, String> params = new HashMap();
        json.entrySet().forEach(entry -> {
            Object value = entry.getValue();
            params.put(entry.getKey(), value == null ? "" : value.toString());
        });

        JSONObject ret = GDTApiUtils.updateAds(advertiser.getAccessToken(), params);
        if (ret == null || ret.getIntValue("code") != 0) {
            log.warn("更新广告失败！param:{},ret={}", json, ret);
            return false;
        }

        return true;
    }
}
