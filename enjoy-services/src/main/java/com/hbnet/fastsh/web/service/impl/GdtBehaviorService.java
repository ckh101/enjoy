package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.web.entity.Agent;
import com.hbnet.fastsh.web.entity.GdtBehavior;
import com.hbnet.fastsh.web.repositories.GdtBehaviorRepository;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GdtBehaviorService {

    @Autowired
    AgentService agentService;


    @Autowired
    GdtBehaviorRepository gdtBehaviorRepository;

    @PersistenceContext(unitName = "entityManagerFactorySmartAd")
    protected EntityManager em;

    public GdtBehavior saveOrUpdate(GdtBehavior gdtBehavior){
        return gdtBehaviorRepository.saveAndFlush(gdtBehavior);
    }


    @Async
    public void syncBehavior(){
        Agent agent = agentService.findById(1L);
        String accessToken = agent.getAccessToken();
        Map<String, String> params = new HashMap<>();
        params.put("type", "BEHAVIOR");
        params.put("tag_spec", "{\"behavior_spec\":{\"query_mode\":\"TARGETING_TAG_QUERY_MODE_COMMON\"}}");
        JSONObject json = GDTApiUtils.getTargetingTags(accessToken, params);
        if(json != null && json.getIntValue("code") == 0){
            JSONArray list = json.getJSONObject("data").getJSONArray("list");
            list.toJavaList(JSONObject.class).forEach(obj->{
                GdtBehavior behavior = new GdtBehavior();
                behavior.setName(obj.getString("name"));
                behavior.setParentName(obj.getString("parent_name"));
                saveOrUpdate(behavior);
            });
        }
    }

    public JSONArray ztreeData(){
        Query nativeQuery=em.createNativeQuery("select name as id, name, parentName as pId, parentName as pName from smartad_gdt_behavior");
        nativeQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> data = nativeQuery.getResultList();
        return JSONArray.parseArray(JSON.toJSONString(data));
    }
}
