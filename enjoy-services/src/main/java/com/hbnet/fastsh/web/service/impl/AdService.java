package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hbnet.fastsh.mongodb.service.impl.AdDailyReportService;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.utils.JsonUtils;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.Ad;
import com.hbnet.fastsh.web.entity.AdGroup;
import com.hbnet.fastsh.web.entity.AdMonitorConfig;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.entity.Campaign;
import com.hbnet.fastsh.web.enums.AdSystemStatusEnum;
import com.hbnet.fastsh.web.enums.ConfiguredStatusEnum;
import com.hbnet.fastsh.web.enums.OP;
import com.hbnet.fastsh.web.enums.SystemStatusEnum;
import com.hbnet.fastsh.web.repositories.AdRepository;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.base.Predication;
import com.hbnet.fastsh.web.service.base.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdService{
    @Autowired
    AdGroupService adGroupService;

    @Autowired
    CampaignService campaignService;

    @Autowired
    AdDailyReportService adDailyReportService;

    @Autowired
    AdRepository adRepository;

    @Autowired
    AdMonitorConfigService adMonitorConfigService;

    public Ad findById(Long id){
        Optional<Ad> optional = adRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }

    public Ad findByIdAndAccountId(Long id, Integer accountId){
        return adRepository.findByIdAndAccountId(id, accountId);
    }

    public Ad findByAdId(Integer adId){
        return adRepository.findByAdId(adId);
    }
    public void page(Ad ad, PageInfo<Ad> adPageInfo){
        Specification<Ad> specification = (root, query, builder)->{
            List<Predicate> predicates = Lists.newArrayList();
            predicates.add(builder.equal(root.get("accountId"), ad.getAccountId()));

            predicates.add(builder.notEqual(root.get("isDeleted"), true));
            Join<AdGroup, AdGroup> joinAdGroup = root.join("adGroup", JoinType.INNER);
            if(!Tools.isBlank(ad.getAdName())){
                predicates.add(builder.like(joinAdGroup.get("adGroupName"),"%"+ad.getAdName()+"%"));
            }
            if(ad.getAdGroup() != null){
                predicates.add(builder.equal(joinAdGroup.get("adGroupId"), ad.getAdGroup().getAdGroupId()));
            }
            predicates.add(builder.notEqual(joinAdGroup.get("isDeleted"), true));
            Join<Campaign, Campaign> joinCampaign = root.join("campaign", JoinType.INNER);
            if(ad.getCampaign() != null){
                if(ad.getCampaign().getId() != null && ad.getCampaign().getId() > 0){
                    predicates.add(builder.equal(joinCampaign.get("id"), ad.getCampaign().getId()));
                }
                if(ad.getCampaign().getCampaignId() != null && ad.getCampaign().getCampaignId() > 0){
                    predicates.add(builder.equal(joinCampaign.get("campaignId"), ad.getCampaign().getCampaignId()));
                }
                if(!Tools.isBlank(ad.getCampaign().getCampaignName())){
                    predicates.add(builder.like(joinCampaign.get("campaignName"),"%"+ad.getCampaign().getCampaignName()+"%"));
                }
            }
            predicates.add(builder.notEqual(joinCampaign.get("isDeleted"), true));
            if(adPageInfo.getStartDate() != null){
                predicates.add(builder.greaterThanOrEqualTo(root.get("createTime"), adPageInfo.getStartDate()));
            }
            if(adPageInfo.getEndDate() != null){
                predicates.add(builder.lessThanOrEqualTo(root.get("createTime"), adPageInfo.getEndDate()));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        adPageInfo.setData(adRepository.findAll(specification, adPageInfo.getLimit(new Sort(Sort.Direction.DESC, "updateTime"))));
    }


    @Transactional
    public Ad saveOrUpdate(Ad ad){
        ad = adRepository.saveAndFlush(ad);
        adMonitorConfigService.createDefaultConfig(ad); // （如不存在）创建默认的预警配置
        return ad;
    }

    @Transactional
    public JSONObject addAdToGDT(Ad ad, Advertiser adv){
        Map<String, String> params = adToMap(ad);
        params.put("adgroup_id", String.valueOf(ad.getAdGroup().getAdGroupId()));
        params.put("adcreative_id", String.valueOf(ad.getAdCreative().getAdCreativeId()));
        JSONObject result = GDTApiUtils.addAds(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }else{
            ad.setAdId(result.getJSONObject("data").getIntValue("ad_id"));
            saveOrUpdate(ad);
        }
        return result;
    }

    @Transactional
    public JSONObject updateAdToGDT(Ad ad, Advertiser adv){
        saveOrUpdate(ad);
        Map<String, String> params = adToMap(ad);
        params.put("ad_id", String.valueOf(ad.getAdId()));
        JSONObject result = GDTApiUtils.updateAds(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }
        return result;
    }

    @Transactional
    public JSONObject delAdByGDT(Ad ad, Advertiser adv){
        saveOrUpdate(ad);
        AdMonitorConfig adMonitorConfig = adMonitorConfigService.findByAdId(ad.getAdId());
        if(adMonitorConfig != null){
            adMonitorConfigService.deleteById(adMonitorConfig.getId());
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("account_id", String.valueOf(ad.getAccountId()));
        params.put("ad_id", String.valueOf(ad.getAdId()));
        JSONObject result = GDTApiUtils.deleteAds(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }
        return result;
    }

    public int deleteByCampaignId(Long campaignId){
        return adRepository.deleteByCampaignId(campaignId);
    }


    public void syncAdByAccountId(Advertiser adv){
        Specification<Ad> specification = (root, query, builder)->{
            List<Predicate> predicates = Lists.newArrayList();
            predicates.add(builder.equal(root.get("accountId"), adv.getAccountId()));
            predicates.add(builder.equal(root.get("isDeleted"), false));
            predicates.add(builder.or(builder.equal(root.get("systemStatus"), AdSystemStatusEnum.AD_STATUS_NORMAL.getValue()), builder.equal(root.get("systemStatus"), AdSystemStatusEnum.AD_STATUS_PENDING.getValue())));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        List<Ad> ads = adRepository.findAll(specification);
        if(ads != null && !ads.isEmpty()){
            JSONObject filter = new JSONObject();
            filter.put("field","ad_id");
            filter.put("operator","IN");
            JSONArray filters = new JSONArray();
            filters.add(filter);
            JSONObject adGroupFilter = new JSONObject();
            adGroupFilter.put("field", "adgroup_id");
            adGroupFilter.put("operator", "IN");
            JSONArray adGroupFilters = new JSONArray();
            adGroupFilters.add(adGroupFilter);
            int page = ads.size()/100;
            if(ads.size()%100 != 0){
                page = page + 1;
            }
            for(int i = 1; i <= page; i++){
                Map<Integer, Ad> adMap = ads.stream().skip((i-1) * 100).limit(100).collect(Collectors.toMap(Ad::getAdId, a -> a,(k1,k2)->k1));
                Map<Integer, AdGroup> adGroupMap = new HashMap<>();
                filter.put("values", adMap.keySet());
                JSONObject json = getAdByGDT(filters, adv, i, 100);
                if(json != null && json.getIntValue("code") == 0){
                    JSONArray list = json.getJSONObject("data").getJSONArray("list");
                    list.toJavaList(JSONObject.class).forEach(obj->{
                        Integer adId = obj.getIntValue("ad_id");
                        String systemStatus = obj.getString("system_status");
                        String rejectMessage = obj.getString("reject_message");
                        String configuredStatus = obj.getString("configured_status");
                        boolean isDeleted = obj.getBooleanValue("is_deleted");
                        Ad ad = adMap.get(adId);
                        if(ad != null){
                            if(!systemStatus.equals(ad.getSystemStatus())||!configuredStatus.equals(ad.getConfiguredStatus())||isDeleted!=ad.getIsDeleted().booleanValue()){
                                ad.setUpdateTime(new Date());
                                ad.setSystemStatus(systemStatus);
                                ad.setRejectMessage(rejectMessage);
                                ad.setConfiguredStatus(configuredStatus);
                                ad.setIsDeleted(isDeleted);
                                saveOrUpdate(ad);
                            }
                            adGroupMap.put(ad.getAdGroup().getAdGroupId(), ad.getAdGroup());
                        }
                    });
                    if(!adGroupMap.isEmpty()){
                        adGroupFilter.put("values", adGroupMap.keySet());
                        JSONObject adGroupJson = adGroupService.getAdGroupsByGDT(adGroupFilters, adv, i, 100);
                        if(adGroupJson != null && adGroupJson.getIntValue("code") == 0){
                            JSONArray adgroups = adGroupJson.getJSONObject("data").getJSONArray("list");
                            adgroups.toJavaList(JSONObject.class).forEach(obj->{
                                Integer adGroupId = obj.getIntValue("adgroup_id");
                                String  beginDate = obj.getString("begin_date");
                                String endDate = obj.getString("end_date");
                                String configuredStatus = obj.getString("configured_status");
                                boolean isDeleted = obj.getBooleanValue("is_deleted");
                                int bidAmount = obj.getIntValue("bid_amount");
                                AdGroup adGroup = adGroupMap.get(adGroupId);
                                adGroup.setBeginDate(beginDate);
                                adGroup.setEndDate(endDate);
                                adGroup.setUpdateTime(new Date());
                                adGroup.setIsDeleted(isDeleted);
                                adGroup.setConfiguredStatus(configuredStatus);
                                adGroup.setBidAmount(bidAmount);
                                adGroupService.saveOrUpdate(adGroup);
                            });
                        }
                    }
                }else{
                    log.warn("获取广告错误："+ json);
                }

            }

        }

    }

    public JSONObject getAdByGDT(JSONArray filtering, Advertiser adv, int page, int pageSize){
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adv.getAccountId()));
        JSONArray fields = new JSONArray();
        fields.add("campaign_id");
        fields.add("adgroup_id");
        fields.add("ad_id");
        fields.add("ad_name");
        fields.add("adcreative");
        fields.add("configured_status");
        fields.add("system_status");
        fields.add("impression_tracking_url");
        fields.add("click_tracking_url");
        fields.add("feeds_interaction_enabled");
        fields.add("reject_message");
        fields.add("is_deleted");
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
        return GDTApiUtils.getAds(adv.getAccessToken(), params);
    }

    public Map<String, String> adToMap(Ad ad){
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(ad.getAccountId()));
        if(!Tools.isBlank(ad.getAdName())){
            params.put("ad_name", ad.getAdGroup().getId()+"-"+ad.getAdName()+"-"+System.currentTimeMillis());
        }
        if(!Tools.isBlank(ad.getConfiguredStatus())){
            params.put("configured_status", ad.getConfiguredStatus());
        }
        if(!Tools.isBlank(ad.getImpressionTrackingUrl())){
            params.put("impression_tracking_url", ad.getImpressionTrackingUrl());
        }
        if(!Tools.isBlank(ad.getClickTrackingUrl())){
            params.put("click_tracking_url", ad.getClickTrackingUrl());
        }
        if(!Tools.isBlank(ad.getFeedsIntegereractionEnabled())){
            params.put("feeds_interaction_enabled", ad.getFeedsIntegereractionEnabled());
        }
        params.put("conversion_tracking_enabled", "true");
        return params;
    }

    public void updateConfiguredStatus(int adId, ConfiguredStatusEnum configuredStatus) {
        Ad ad = adRepository.findByAdId(adId);
        ad.setConfiguredStatus(configuredStatus.getValue());
        ad.setUpdateTime(new Date());

        adRepository.save(ad);
    }

    public Ad findByAdIdAndAccountId(int adId, int accountId) {
        return adRepository.findByAdIdAndAccountId(adId, accountId);
    }

    public List<Ad> findAll() {
        return adRepository.findAll();
    }

    public Ad findByAdCreativeId(Long adcreativeId){
        return adRepository.findByAdCreativeId(adcreativeId);
    }

    /**
     * 检查accountId和对应的广告表记录是否全部一致
     * @param accountId
     * @param idList
     * @return
     */
    public boolean checkAdOwner(Integer accountId, List<Long> idList) {
        List<Long> dbIdList = adRepository.findIdByAccountIdAndIdIn(accountId, idList);
        return dbIdList.size() == idList.size();
    }

    public List<Ad> findByIds(List<Long> ids) {
        return adRepository.findAllById(ids);
    }
}
