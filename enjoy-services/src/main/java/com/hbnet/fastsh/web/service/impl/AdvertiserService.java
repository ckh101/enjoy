package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.cache.CacheClient;
import com.hbnet.fastsh.web.entity.*;
import com.hbnet.fastsh.web.enums.OP;
import com.hbnet.fastsh.web.enums.SystemStatusEnum;
import com.hbnet.fastsh.web.repositories.AdvertiserRepository;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.base.Predication;
import com.hbnet.fastsh.web.service.base.SpecificationFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@Slf4j
public class AdvertiserService{

    @Autowired
    AgentService agentService;

    @Autowired
    AdvertiserWechatSpecService advertiserWechatSpecService;

    @Autowired
    AdvertiserFundService advertiserFundService;

    @Autowired
    CacheClient cc;

    @Autowired
    AdvertiserRepository advertiserRepository;


    public Advertiser saveOrUpdate(Advertiser advertiser){
        return advertiserRepository.saveAndFlush(advertiser);
    }

    public Advertiser findById(Long id){
        Optional<Advertiser> optional = advertiserRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }
    public void page(Advertiser adv, PageInfo<Advertiser> pageInfo, User user){
        Specification<Advertiser> specification = (root, query, builder)->{
            List<Predicate> predicates = Lists.newArrayList();
            if(!Tools.isBlank(adv.getCorporationName())) {
                predicates.add(builder.like(root.get("corporationName"), "%" + adv.getCorporationName() + "%"));
            }
            if(adv.getWxAuthStatus() != null && adv.getWxAuthStatus() > 0){
                predicates.add(builder.equal(root.get("wxAuthStatus"), adv.getWxAuthStatus()));
            }

            if(adv.getAuthStatus() != null && adv.getAuthStatus() > 0){
                predicates.add(builder.equal(root.get("authStatus"), adv.getWxAuthStatus()));
            }
            if(adv.getAccountId() != null && adv.getAccountId() > 0){
                predicates.add(builder.equal(root.get("accountId"), adv.getAccountId()));
            }
            if(user.getIsRoot() != 1){
                if(!("sys".equals(user.getRole().getFlagStr())&&"SYSTEM".equals(user.getAccountType()))){
                    Join<User, User> joinUser = root.join("users", JoinType.INNER);
                    predicates.add(builder.equal(joinUser.get("id"), user.getId()));
                }
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        pageInfo.setData(advertiserRepository.findAll(specification, pageInfo.getLimit(new Sort(Sort.Direction.DESC, "createTime"))));

    }

    public Long countWxAuth(){
        return advertiserRepository.countByWxAppIdNotNullAndWxAuthStatusEquals(1);
    }
    public long countAuth(){
        return advertiserRepository.countByAuthStatusEquals(1);
    }
    public boolean isExist(Integer accountId) {
        return advertiserRepository.existsByAccountIdEquals(accountId);
    }

    public Advertiser findByAccountId(Integer accountId){
        return advertiserRepository.findFirstByAccountId(accountId);
    }

    public JSONObject getByGdt(Advertiser advertiser, boolean isAgent){
        String accessToken;
        if(isAgent){
            accessToken = agentService.getAccessToken(advertiser.getAgentId());
        }else{
            accessToken = advertiser.getAccessToken();
        }
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(advertiser.getAccountId()));
        params.put("page", "1");
        params.put("page_size", "1");
        JSONArray fields = new JSONArray();
        fields.add("daily_budget");
        fields.add("system_status");
        fields.add("reject_message");
        fields.add("corporation_name");
        fields.add("corporation_licence");
        fields.add("certification_image");
        fields.add("identity_number");
        fields.add("corporate_image_name");
        fields.add("corporate_image_logo");
        fields.add("system_industry_id");
        fields.add("customized_industry");
        fields.add("introduction_url");
        fields.add("contact_person");
        fields.add("contact_person_email");
        fields.add("contact_person_telephone");
        fields.add("contact_person_mobile");
        fields.add("wechat_spec");
        params.put("fields", fields.toJSONString());
        JSONObject advertiserJson = GDTApiUtils.getAdvertiser(accessToken, params);
        return advertiserJson;
    }

    public void syncAdvInfo(Advertiser adv){
        JSONObject advertiserJson = getByGdt(adv, false);
        if(advertiserJson != null && advertiserJson.getIntValue("code") == 0){
            JSONObject data = advertiserJson.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            if(list.size() > 0){
                JSONObject json = list.getJSONObject(0);
                setInfo(adv, json);
                if(json.containsKey("wechat_spec")){
                    adv.setWxAuthStatus(1);
                    JSONObject advertiserWechatSpecJson = json.getJSONObject("wechat_spec");
                    advertiserWechatSpecService.updateOrSaveAdvertiserWechatSpec(advertiserWechatSpecJson, adv.getAccountId());
                }
                saveOrUpdate(adv);
                advertiserFundService.updateOrSaveFunds(adv);
            }
        }
    }

    public void setInfo(Advertiser adv, JSONObject json){
        adv.setDailyBudget(json.getIntValue("daily_budget"));
        adv.setSystemStatus(json.getString("system_status"));
        adv.setRejectMessage(json.getString("reject_message"));
        adv.setCorporationName(json.getString("corporation_name"));
        adv.setCorporationLicence(json.getString("corporation_licence"));
        adv.setCertificationImage(json.getString("certification_image"));
        adv.setIdentityNumber(json.getString("identity_number"));
        adv.setCorporateImageName(json.getString("corporate_image_name"));
        adv.setCorporateImageLogo(json.getString("corporate_image_logo"));
        adv.setSystemIndustryId(json.getIntValue("system_industry_id"));
        adv.setCustomizedIndustry(json.getString("customized_industry"));
        adv.setIntroductionUrl(json.getString("introduction_url"));
        adv.setContactPerson(json.getString("contact_person"));
        adv.setContactPersonEmail(json.getString("contact_person_email"));
        adv.setContactPersonMobile(json.getString("contact_person_mobile"));
        adv.setContactPersonTelephone(json.getString("contact_person_telephone"));
        adv.setUpdateTime(new Date());
    }

    public JSONObject refreshToken(Long id){
        Advertiser adv = findById(id);
        long nowTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        JSONObject result = new JSONObject();
        if(cc.get(id+"_refreshToken") != null){
            result.put("errorcode", 10001);
            result.put("msg", "请求频繁");
            return result;
        }
        if(adv.getRefreshExpiresTime().getTime() > nowTime){
            JSONObject json = GDTApiUtils.refreshAccessToken(adv.getRefreshAccessToken());
            if(json != null) {
                cc.set(id+"_refreshToken", 1, Tools.addMinute(new Date(), 30));
                if (json.getIntValue("code") == 0) {
                    String token = json.getJSONObject("data").getString("access_token");
                    int expiresIn = json.getJSONObject("data").getIntValue("access_token_expires_in");
                    adv.setAccessToken(token);
                    adv.setExpiresTime(Tools.addSeconds(new Date(), expiresIn - 600));
                    adv.setRefreshExpiresTime(Tools.addDay(new Date(), 30));
                    saveOrUpdate(adv);
                    result.put("errorcode", 0);
                    result.put("msg", "刷新成功");
                } else {
                    log.warn("广告主："+id+"刷新token失败"+json);
                    result.put("errorcode", 1);
                    result.put("msg", "刷新accessToken失败，请联系服务商");
                    result.put("data", json);
                }
            }else{
                log.warn("广告主："+id+"刷新token失败"+json);
                result.put("errorcode", 1);
                result.put("msg", "服务器异常，请联系服务商");
            }
        }else{
            log.warn("广告主："+id+"刷新token失败,refreshAccessToken过期，请联系服务商");
            result.put("errorcode", 2);
            result.put("msg", "refreshAccessToken过期，请联系服务商");
        }
        return result;
    }

    public JSONArray getLast7DaysData(Advertiser adv){
        String key = "Last7DaysData_"+adv.getAccountId();
        Object data = cc.get(key);
        if(data != null){
            return (JSONArray)data;
        }
        String startDate = Tools.format(Tools.addDay(new Date(), -7));
        String endDate = Tools.format(Tools.addDay(new Date(), -7));
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(adv.getAccountId()));
        params.put("level", "REPORT_LEVEL_ADVERTISER");
        params.put("date_range", "{\"start_date\":\"+startDate+\",\"end_date\":\"+endDate+\"}");
        JSONArray fields = new JSONArray();
        fields.add("date");
        fields.add("view_count");
        fields.add("valid_click_count");
        params.put("fields", fields.toJSONString());
        JSONObject json = GDTApiUtils.getDailyReports(adv.getAccessToken(), params);
        JSONArray array = null;
        if(json != null && json.getIntValue("code") == 0){
            array = json.getJSONObject("data").getJSONArray("list");
        }else{
            array = new JSONArray();
        }
        cc.set(key, array, Tools.getTodayLast());
        return array;
    }

    public long countNeedRefreshToken(){
        List<Predication> ps = Lists.newArrayList();
        ps.add(Predication.get(OP.LTEQ,"expiresTime", new Date()));
        ps.add(Predication.get(OP.EQ,"authStatus", 1));
        ps.add(Predication.get(OP.EQ,"systemStatus", SystemStatusEnum.CUSTOMER_STATUS_NORMAL.getValue()));
        return advertiserRepository.count(SpecificationFactory.where(ps));
    }

    public List<Advertiser> getNewRefreshTokenPageList(PageInfo<Advertiser> pageInfo){
        List<Predication> ps = Lists.newArrayList();
        ps.add(Predication.get(OP.LTEQ,"expiresTime", new Date()));
        ps.add(Predication.get(OP.EQ,"authStatus", 1));
        ps.add(Predication.get(OP.EQ,"systemStatus", SystemStatusEnum.CUSTOMER_STATUS_NORMAL.getValue()));
        pageInfo.setData(advertiserRepository.findAll(SpecificationFactory.where(ps),pageInfo.getLimit()));
        return pageInfo.getList();
    }

    public Long countNorMalAdvertiser(){
        List<Predication> ps = Lists.newArrayList();
        ps.add(Predication.get(OP.EQ,"authStatus", 1));
        return advertiserRepository.count(SpecificationFactory.where(ps));
    }

    public List<Advertiser> getNorMalAdvertiserPageList(PageInfo<Advertiser> pageInfo){
        List<Predication> ps = Lists.newArrayList();
        ps.add(Predication.get(OP.EQ,"authStatus", 1));
        ps.add(Predication.get(OP.EQ,"systemStatus", SystemStatusEnum.CUSTOMER_STATUS_NORMAL.getValue()));
        pageInfo.setData(advertiserRepository.findAll(SpecificationFactory.where(ps),pageInfo.getLimit()));
        return pageInfo.getList();
    }

    public List<Advertiser> list(){
        return advertiserRepository.findAll();
    }

    public List<Advertiser> getAllNorMalAdvertiserPageList(){
        return advertiserRepository.findAllByAuthStatusAndSystemStatus(1, SystemStatusEnum.CUSTOMER_STATUS_NORMAL.getValue());
    }
    public List<Advertiser> findByIdIn(Long[] ids){
        return advertiserRepository.findByIdIn(ids);
    }

    public Advertiser findByUserIdAndId(Long userId, Long Id){
        return advertiserRepository.findByUserIdAndId(userId, Id);
    }
}
