package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbnet.fastsh.redis.service.RedisService;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.utils.JsonUtils;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.Agent;
import com.hbnet.fastsh.web.entity.GdtLocation;
import com.hbnet.fastsh.web.repositories.GdtLocationRepository;
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
public class GdtLocationService{
    @Autowired
    AgentService agentService;

    @Autowired
    RedisService redisService;

    @Autowired
    GdtLocationRepository gdtLocationRepository;

    @PersistenceContext(unitName = "entityManagerFactorySmartAd")
    protected EntityManager em;

    public GdtLocation saveOrUpdate(GdtLocation gdtLocation){
        return gdtLocationRepository.saveAndFlush(gdtLocation);
    }

    @Async
    public void getLocation(){
        Agent agent = agentService.findById(1L);
        String accessToken = agent.getAccessToken();
        Thread regions = new Thread(()->{
            Map<String, String> params = new HashMap<>();
            params.put("type", "REGION");
            JSONObject json = GDTApiUtils.getTargetingTags(accessToken, params);
            if(json != null && json.getIntValue("code") == 0){
                JSONArray list = json.getJSONObject("data").getJSONArray("list");
                list.toJavaList(JSONObject.class).forEach(obj->{
                    GdtLocation loc = new GdtLocation();
                    loc.setAreaType("region");
                    loc.jsonToObj(obj);
                    saveOrUpdate(loc);
                });
            }
        });
        Thread business = new Thread(()->{
            Map<String, String> params = new HashMap<>();
            params.put("type", "BUSINESS_DISTRICT");
            JSONObject json = GDTApiUtils.getTargetingTags(accessToken, params);
            if(json != null && json.getIntValue("code") == 0){
                JSONArray list = json.getJSONObject("data").getJSONArray("list");
                list.toJavaList(JSONObject.class).forEach(obj->{
                    GdtLocation loc = new GdtLocation();
                    loc.setAreaType("business");
                    loc.jsonToObj(obj);
                    saveOrUpdate(loc);
                });
            }
        });
        regions.start();
        business.start();
    }

    public void setParentName(Long parentId, String parentName){
        List<GdtLocation> list = gdtLocationRepository.findByParentId(parentId);
        if(list != null && !list.isEmpty()){
            list.forEach(obj->{
                if(parentName.equals("")){
                    obj.setParentName(obj.getCityName());
                }else{
                    obj.setParentName(parentName);
                }

                saveOrUpdate(obj);
                if(obj.getParentName().equals(obj.getCityName())){
                    setParentName(obj.getId(), obj.getParentName());
                }else{
                    setParentName(obj.getId(), obj.getParentName()+"-"+obj.getCityName());
                }

            });
        }
    }

    public String getTreeJson(){
        String tree = redisService.get("smartad_gdt_location");
        if(Tools.isBlank(tree)){
            GdtLocation loc = new GdtLocation();
            loc.setId(0L);
            getTree(loc);
            tree = JsonUtils.toJson(loc.getChildren()).replaceAll("cityName", "text");
            redisService.set("smartad_gdt_location", tree, 0);
        }
        return tree;
    }

    public void getTree(GdtLocation loc){
        List<GdtLocation> children = gdtLocationRepository.findByParentId(loc.getId());
        if(children != null && !children.isEmpty()){
            loc.setChildren(children);
            children.forEach(child->{
                getTree(child);
            });
        }
    }

    public JSONArray ztreeData(){
        Query nativeQuery=em.createNativeQuery("select id,cityName as name, parentId as pId, parentName as pName, areatype from smartad_gdt_location  where parentName <> \"\"");
        nativeQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> data = nativeQuery.getResultList();
        return JSONArray.parseArray(JSON.toJSONString(data));
    }
}
