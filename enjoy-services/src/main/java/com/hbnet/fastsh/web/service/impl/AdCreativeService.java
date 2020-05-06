package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.Ad;
import com.hbnet.fastsh.web.entity.AdCreative;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.enums.AdSystemStatusEnum;
import com.hbnet.fastsh.web.enums.SystemStatusEnum;
import com.hbnet.fastsh.web.repositories.AdCreativeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class AdCreativeService{

    @Autowired
    AdCreativeRepository adCreativeRespository;

    @Autowired
    AdService adService;

    public AdCreative findById(Long id){
        Optional<AdCreative> optional = adCreativeRespository.findById(id);
        return optional.isPresent()?optional.get():null;
    }

    public AdCreative saveOrUpdate(AdCreative adCreative){
        return adCreativeRespository.saveAndFlush(adCreative);
    }

    public AdCreative findByAdCreativeIdAndAccountId(Integer adcreativeId, Integer accountId){
        return adCreativeRespository.findByAdCreativeIdAndAccountId(adcreativeId, accountId);
    }

    public AdCreative findByIdAndAccountId(Long id, Integer accountId){
        return adCreativeRespository.findByIdAndAccountId(id, accountId);
    }

    public AdCreative findByPrivateCampaignId(Long privateCampaignId){
        return adCreativeRespository.findByPrivateCampaignId(privateCampaignId);
    }

    @Transactional
    public JSONObject addAdCreativeToGDT(AdCreative adCreative, Advertiser adv){
        Map<String, String> params = adCreativeToMap(adCreative);
        params.put("account_id", String.valueOf(adCreative.getAccountId()));
        params.put("campaign_id", String.valueOf(adCreative.getCampaignId()));
        JSONObject result = GDTApiUtils.addAdcreatives(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }else{
            adCreative.setAdCreativeId(result.getJSONObject("data").getIntValue("adcreative_id"));
            saveOrUpdate(adCreative);
        }
        return result;
    }

    @Transactional
    public JSONObject updateAdCreativeToGDT(AdCreative adCreative, Advertiser adv){
        saveOrUpdate(adCreative);
        Ad ad = adService.findByAdCreativeId(adCreative.getId());
        if(ad != null){
            ad.setSystemStatus(AdSystemStatusEnum.AD_STATUS_PENDING.getValue());
            adService.saveOrUpdate(ad);
        }
        Map<String, String> params = adCreativeToMap(adCreative);
        params.put("account_id", String.valueOf(adCreative.getAccountId()));
        params.put("adcreative_id", String.valueOf(adCreative.getAdCreativeId()));
        JSONObject result = GDTApiUtils.updateAdcreatives(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }
        return result;
    }

    @Transactional
    public JSONObject delAdCreativeByGDT(AdCreative adCreative, Advertiser adv){
        saveOrUpdate(adCreative);
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adCreative.getAccountId()));
        params.put("adcreative_id", String.valueOf(adCreative.getAdCreativeId()));
        JSONObject result = GDTApiUtils.deleteAdcreatives(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }
        return result;
    }
    public JSONObject getAdCreativeByGDT(JSONArray filtering, Advertiser adv, int page, int pageSize){
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adv.getAccountId()));
        JSONArray fields = new JSONArray();
        fields.add("campaign_id");
        fields.add("adcreative_id");
        fields.add("adcreative_name");
        fields.add("adcreative_template_id");
        fields.add("adcreative_elements");
        fields.add("page_type");
        fields.add("page_spec");
        fields.add("site_set");
        fields.add("promoted_object_type");
        fields.add("promoted_object_id");
        fields.add("share_content_spec");
        fields.add("dynamic_adcreative_spec");
        fields.add("is_deleted");
        fields.add("multi_share_optimization_enabled");
        params.put("fields", fields.toJSONString());
        if(filtering != null){
            params.put("filtering", filtering.toJSONString());
        }
        if(page > 0){
            params.put("page", String.valueOf(page));
        }
        if(pageSize > 0){
            params.put("page_size", String.valueOf(pageSize));
        }
        return GDTApiUtils.getAdcreatives(adv.getAccessToken(), params);
    }
    public Map<String, String> adCreativeToMap(AdCreative adCreative){
        Map<String, String> params = new HashMap<>();
        params.put("adcreative_name", adCreative.getId()+"-"+adCreative.getAdCreativeName()+"-"+System.currentTimeMillis());
        params.put("adcreative_template_id", String.valueOf(adCreative.getAdCreativeTemplateId()));
        params.put("adcreative_elements", adCreative.getAdCreativeElements());
        params.put("site_set", adCreative.getSiteSet());
        params.put("promoted_object_type", adCreative.getPromotedObjectType());
        params.put("page_type", adCreative.getPageType());
        if(!Tools.isBlank(adCreative.getPageSpec())){
            params.put("page_spec", adCreative.getPageSpec());
        }

        if(!Tools.isBlank(adCreative.getPromotedObjectId())){
            params.put("promoted_object_id", adCreative.getPromotedObjectId());
        }

        if(!Tools.isBlank(adCreative.getShareContentSpec())){
            params.put("share_content_spec", adCreative.getShareContentSpec());
        }

        if(!Tools.isBlank(adCreative.getDynamicAdCreativeSpec())){
            params.put("dynamic_adcreative_spec", adCreative.getDynamicAdCreativeSpec());
        }
        if(!Tools.isBlank(adCreative.getMultiShareOptimizationEnabled())){
            params.put("multi_share_optimization_enabled", adCreative.getMultiShareOptimizationEnabled());
        }
        return params;

    }
}
