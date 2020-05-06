package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hbnet.fastsh.mongodb.service.impl.CampaignDailyReportService;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.Ad;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.entity.Campaign;
import com.hbnet.fastsh.web.enums.CampaignsTypeEnum;
import com.hbnet.fastsh.web.enums.ConfiguredStatusEnum;
import com.hbnet.fastsh.web.enums.OP;
import com.hbnet.fastsh.web.repositories.CampaignRepository;
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

import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CampaignService{

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    AdService adService;

    @Autowired
    CampaignDailyReportService campaignDailyReportService;

    public Campaign findById(Long id){
        Optional<Campaign> optional = campaignRepository.findById(id);

        return optional.isPresent()?optional.get():null;
    }
    public void page(Campaign campaign, PageInfo<Campaign> campaignPageInfo){
        List<Predication> ps = Lists.newArrayList();
        ps.add(Predication.get(OP.EQ, "accountId",campaign.getAccountId()));
        if(!Tools.isBlank(campaign.getCampaignName())){
            ps.add(Predication.get(OP.LIKE, "campaignName", "%"+campaign.getCampaignName()+"%"));
        }
        if(campaign.getCampaignId() != null){
            ps.add(Predication.get(OP.EQ, "campaignId", campaign.getCampaignId()));
        }
        if(campaignPageInfo.getStartDate() != null){
            ps.add(Predication.get(OP.GTEQ, "createTime", campaignPageInfo.getStartDate()));
        }
        if(campaignPageInfo.getEndDate() != null){
            ps.add(Predication.get(OP.LTEQ, "createTime", campaignPageInfo.getEndDate()));
        }
        ps.add(Predication.get(OP.NOTEQ, "isDeleted", true));
        campaignPageInfo.setData(campaignRepository.findAll(SpecificationFactory.where(ps),campaignPageInfo.getLimit(new Sort(Sort.Direction.DESC, "updateTime"))));
        /*List<Campaign> campaigns = campaignPageInfo.getList();
        if(campaigns != null){
            campaigns.parallelStream().forEach(campaign1 -> {campaign1.setCampaignDailyReport(campaignDailyReportService.findLastDailyReport(campaign1.getCampaignId()));});
        }*/

    }

    public List<Campaign> findAllByAccountId(Integer accountId){
        List<Predication> ps = Lists.newArrayList();
        ps.add(Predication.get(OP.EQ, "accountId", accountId));
        ps.add(Predication.get(OP.NOTEQ, "isDeleted", 1));
        return campaignRepository.findAll(SpecificationFactory.where(ps));
    }

    public Campaign findByCampaignId(Integer campaignId){
        return campaignRepository.findByCampaignId(campaignId);
    }

    public Campaign findByCampaignIdAndAccountId(Integer campaignId, Integer accountId){
        return campaignRepository.findByCampaignIdAndAccountId(campaignId, accountId);
    }

    public Campaign findByIdAndAccountId(Long id, Integer accountId){
        return campaignRepository.findByIdAndAccountId(id, accountId);
    }

    public Campaign saveOrUpdate(Campaign campaign){
        if("CAMPAIGN_TYPE_WECHAT_OFFICIAL_ACCOUNTS".equals(campaign.getCampaignType())){
            campaign.setCampaignType(CampaignsTypeEnum.CAMPAIGN_TYPE_NORMAL.getValue());
        }
        campaign = campaignRepository.saveAndFlush(campaign);
        return campaign;
    }


    @Transactional
    public JSONObject addCampaignToGDT(Campaign campaign, Advertiser adv){
        Map<String, String> params = campaignToMap(campaign);
        params.put("account_id", String.valueOf(adv.getAccountId()));
        JSONObject result = GDTApiUtils.addCampaigns(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }else{
            campaign.setCampaignId(result.getJSONObject("data").getIntValue("campaign_id"));
            saveOrUpdate(campaign);
        }
        return result;
    }

    @Transactional
    public JSONObject updateCampaignToGDT(Campaign campaign, Advertiser adv){
        saveOrUpdate(campaign);
        Map<String, String> params = campaignToMap(campaign);
        params.put("account_id", String.valueOf(adv.getAccountId()));
        params.put("campaign_id", String.valueOf(campaign.getCampaignId()));
        JSONObject result = GDTApiUtils.updateCampaigns(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }
        return result;
    }

    @Transactional
    public JSONObject setCampaignConfiguredStatusToGDT(Campaign campaign, Advertiser adv){
        saveOrUpdate(campaign);
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adv.getAccountId()));
        params.put("campaign_id", String.valueOf(campaign.getCampaignId()));
        params.put("configured_status", campaign.getConfiguredStatus());
        JSONObject result = GDTApiUtils.updateCampaigns(adv.getAccessToken(), params);
        if(result == null || result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }
        return result;
    }

    public JSONObject getCampaignByGDT(JSONArray filtering, Advertiser adv, int page, int pageSize){
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adv.getAccountId()));
        JSONArray fields = new JSONArray();
        fields.add("campaign_id");
        fields.add("campaign_name");
        fields.add("configured_status");
        fields.add("campaign_type");
        fields.add("promoted_object_type");
        fields.add("daily_budget");
        fields.add("budget_reach_date");
        fields.add("speed_mode");
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
        return GDTApiUtils.getCampaigns(adv.getAccessToken(), params);
    }
    @Transactional
    public JSONObject delCampaignByGDT(Campaign campaign, Advertiser adv){
        campaign.setIsDeleted(true);
        saveOrUpdate(campaign);
        adService.deleteByCampaignId(campaign.getId());
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adv.getAccountId()));
        params.put("campaign_id", String.valueOf(campaign.getCampaignId()));
        JSONObject result = GDTApiUtils.deleteCampaigns(adv.getAccessToken(),params);
        if(result == null||result.getIntValue("code") != 0){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(result.toString());
        }
        return result;
    }

    private Map<String, String> campaignToMap(Campaign campaign){
        Map<String, String> params = new HashMap<>();
        params.put("campaign_name", campaign.getId()+"-"+campaign.getCampaignName()+"-"+System.currentTimeMillis());
        params.put("campaign_type", campaign.getCampaignType());
        params.put("promoted_object_type", campaign.getPromotedObjectType());
        if(campaign.getDailyBudget() != null && campaign.getDailyBudget() >0){
            params.put("daily_budget", String.valueOf(campaign.getDailyBudget()));
        }
        params.put("configured_status", campaign.getConfiguredStatus());
        params.put("speed_mode", campaign.getSpeedMode());
        return params;
    }

    public void syncCampaignByAccountId(Advertiser adv){
        Specification<Campaign> specification = (root, query, builder)->{
            List<Predicate> predicates = Lists.newArrayList();
            predicates.add(builder.equal(root.get("accountId"), adv.getAccountId()));
            predicates.add(builder.equal(root.get("isDeleted"), false));
            predicates.add(builder.isNotNull(root.get("campaignId")));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        List<Campaign> campaigns = campaignRepository.findAll(specification);
        if(campaigns != null && !campaigns.isEmpty()){
            JSONObject filter = new JSONObject();
            filter.put("field","campaign_id");
            filter.put("operator","IN");
            JSONArray filters = new JSONArray();
            filters.add(filter);
            int page = campaigns.size()/100;
            if(campaigns.size()%100 != 0){
                page = page + 1;
            }
            for(int i = 1; i <= page; i++){
                Map<Integer, Campaign> campaignMap = campaigns.stream().skip((i-1) * 100).limit(100).collect(Collectors.toMap(Campaign::getCampaignId, a -> a,(k1, k2)->k1));
                filter.put("values", campaignMap.keySet());
                JSONObject json = getCampaignByGDT(filters, adv, i, 100);
                if(json != null && json.getIntValue("code") == 0){
                    JSONArray list = json.getJSONObject("data").getJSONArray("list");
                    list.toJavaList(JSONObject.class).forEach(obj->{
                        Integer campaignId = obj.getInteger("campaign_id");
                        String configuredStatus = obj.getString("configured_status");
                        int dailyBudget = obj.getIntValue("daily_budget");
                        Integer budgetReachDate = obj.getInteger("budget_reach_date");
                        boolean isDeleted = obj.getBooleanValue("is_deleted");
                        Campaign campaign = campaignMap.get(campaignId);
                        if(campaign != null){
                            if(!configuredStatus.equals(campaign.getConfiguredStatus())||dailyBudget!=campaign.getDailyBudget()||budgetReachDate!=null||isDeleted!=campaign.getIsDeleted().booleanValue()){
                                campaign.setConfiguredStatus(configuredStatus);
                                campaign.setDailyBudget(dailyBudget);
                                campaign.setBudgetReachDate(budgetReachDate!=null?budgetReachDate.toString():null);
                                campaign.setIsDeleted(isDeleted);
                                saveOrUpdate(campaign);
                            }
                        }
                    });
                }else{
                    log.warn("获取计划错误："+ json);
                }
            }
        }
    }
}
