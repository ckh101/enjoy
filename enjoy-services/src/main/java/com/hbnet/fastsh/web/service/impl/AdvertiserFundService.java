package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.web.entity.Advertiser;
import com.hbnet.fastsh.web.entity.AdvertiserFund;
import com.hbnet.fastsh.web.repositories.AdvertiserFundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdvertiserFundService{

    @Autowired
    AdvertiserFundRepository advertiserFundRepository;

    public AdvertiserFund saveOrUpdate(AdvertiserFund advertiserFund){
        return advertiserFundRepository.saveAndFlush(advertiserFund);
    }

    public void updateOrSaveFunds(Advertiser advertiser){
        Map<String, String> params = new HashMap<>();
        params.put("account_id", String.valueOf(advertiser.getAccountId()));
        JSONObject fundsJson = GDTApiUtils.getFunds(advertiser.getAccessToken(), params);
        if(fundsJson != null){
            if(fundsJson.getIntValue("code") == 0){
                JSONArray list = fundsJson.getJSONObject("data").getJSONArray("list");
                list.toJavaList(JSONObject.class).forEach(json->{
                    AdvertiserFund fund = findByAccountIdAndFundType(advertiser.getAccountId(), json.getString("fund_type"));
                    if(fund == null){
                        fund = new AdvertiserFund();
                        fund.setCreateTime(new Date());
                    }
                    fund.setAccountId(advertiser.getAccountId());
                    fund.setFundType(json.getString("fund_type"));
                    fund.setBalance(json.getIntValue("balance"));
                    fund.setFundStatus(json.getString("fund_status"));
                    fund.setRealtimeCost(json.getIntValue("realtime_cost"));
                    saveOrUpdate(fund);
                });
            }
        }
    }

    public AdvertiserFund findByAccountIdAndFundType(Integer accountId, String fundType){
        return advertiserFundRepository.findFirstByAccountIdAndFundType(accountId,fundType);
    }

    public List<AdvertiserFund> getFundsByAccountId(Integer accountId){
        return advertiserFundRepository.getFundsByAccountId(accountId);
    }
}
