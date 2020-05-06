package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.web.entity.AdvertiserWechatSpec;
import com.hbnet.fastsh.web.repositories.AdvertiserWechatSpecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdvertiserWechatSpecService {

    @Autowired
    AdvertiserWechatSpecRepository advertiserWechatSpecRepository;

    public AdvertiserWechatSpec findByAccountId(Integer accountId, String wechatAccountId){

        return advertiserWechatSpecRepository.findFirstByAccountIdAndWechatAccountId(accountId, wechatAccountId);
    }

    public AdvertiserWechatSpec getByWxAppId(String wechatAccountId){
        return advertiserWechatSpecRepository.findFirstByWechatAccountId(wechatAccountId);
    }

    public AdvertiserWechatSpec saveOrUpdate(AdvertiserWechatSpec advertiserWechatSpec){
        return advertiserWechatSpecRepository.saveAndFlush(advertiserWechatSpec);
    }

    public void updateOrSaveAdvertiserWechatSpec(JSONObject advertiserWechatSpecJson, int accountId){
        AdvertiserWechatSpec advertiserWechatSpec = findByAccountId(accountId, advertiserWechatSpecJson.getString("wechat_account_id"));
        if(advertiserWechatSpec == null){
            advertiserWechatSpec = new AdvertiserWechatSpec();
            advertiserWechatSpec.setCreateTime(new Date());
        }
        advertiserWechatSpec.setAccountId(accountId);
        advertiserWechatSpec.setWechatAccountId(advertiserWechatSpecJson.getString("wechat_account_id"));
        advertiserWechatSpec.setWechatAccountName(advertiserWechatSpecJson.getString("wechat_account_name"));
        advertiserWechatSpec.setSystemStatus(advertiserWechatSpecJson.getString("system_status"));
        advertiserWechatSpec.setSystemIndustryId(advertiserWechatSpecJson.getIntValue("system_industry_id"));
        advertiserWechatSpec.setIndustryName(advertiserWechatSpecJson.getString("industry_name"));
        advertiserWechatSpec.setContactPerson(advertiserWechatSpecJson.getString("contact_person"));
        advertiserWechatSpec.setContactPersonTelephone(advertiserWechatSpecJson.getString("contact_person_telephone"));
        advertiserWechatSpec.setBusinessType(advertiserWechatSpecJson.getString("business_type"));
        advertiserWechatSpec.setBusinessContent(advertiserWechatSpecJson.getString("business_content"));
        advertiserWechatSpec.setRejectMessage(advertiserWechatSpecJson.getString("reject_message"));
        saveOrUpdate(advertiserWechatSpec);
    }
}
