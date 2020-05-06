package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.web.entity.Agent;
import com.hbnet.fastsh.web.entity.WechatOfficialAccountCategory;
import com.hbnet.fastsh.web.repositories.WechatOfficalAccountCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WechatOfficalAccountGactegoryService {

    @Autowired
    AgentService agentService;

    @Autowired
    WechatOfficalAccountCategoryRepository wechatOfficalAccountCategoryRepository;

    public WechatOfficialAccountCategory saveOrUpdate(WechatOfficialAccountCategory wechatOfficialAccountCategory){
        return wechatOfficalAccountCategoryRepository.saveAndFlush(wechatOfficialAccountCategory);
    }

    @Async
    public void syncWechatOfficalAccountCategory(){
        Agent agent = agentService.findById(1L);
        String accessToken = agent.getAccessToken();
        Map<String, String> params = new HashMap<>();
        params.put("type", "WECHAT_OFFICIAL_ACCOUNT_CATEGORY");
        JSONObject json = GDTApiUtils.getTargetingTags(accessToken, params);
        if(json != null && json.getIntValue("code") == 0) {
            JSONArray list = json.getJSONObject("data").getJSONArray("list");
            list.toJavaList(JSONObject.class).forEach(obj -> {
                WechatOfficialAccountCategory wechatOfficialAccountCategory = new WechatOfficialAccountCategory();
                wechatOfficialAccountCategory.setId(obj.getLong("id"));
                wechatOfficialAccountCategory.setName(obj.getString("name"));
                saveOrUpdate(wechatOfficialAccountCategory);
            });
        }
    }

    public List<WechatOfficialAccountCategory> getList(){

        return wechatOfficalAccountCategoryRepository.findAll();
    }
}
