package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hbnet.fastsh.mongodb.model.GdtOrder;
import com.hbnet.fastsh.mongodb.service.impl.GdtOrderService;
import com.hbnet.fastsh.utils.GDTApiUtils;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.cache.CacheClient;
import com.hbnet.fastsh.web.entity.Agent;
import com.hbnet.fastsh.web.repositories.AgentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@Slf4j
public class AgentService{

    @Autowired
    CacheClient cc;

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    GdtOrderService gdtOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public Agent findById(Long id){
        Optional<Agent> optional = agentRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }
    public String getAccessToken(Long id){
        Agent agent = this.findById(id);
        return agent.getAccessToken();
    }

    public Agent saveOrUpdate(Agent agent){
        return agentRepository.saveAndFlush(agent);
    }
    public JSONObject refreshToken(Long id){
        Agent agent = this.findById(id);
        long nowTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        JSONObject result = new JSONObject();
        if(cc.get(id+"_refreshToken") != null){
            result.put("errorcode", 10001);
            result.put("msg", "请求频繁");
            return result;
        }
        if(agent.getRefreshExpiresTime().getTime() > nowTime){
            JSONObject json = GDTApiUtils.refreshAccessToken(agent.getRefreshAccessToken());
            if(json != null) {
                cc.set(id+"_refreshToken", 1, Tools.addMinute(new Date(), 30));
                if (json.getIntValue("code") == 0) {
                    String token = json.getJSONObject("data").getString("access_token");
                    int expiresIn = json.getJSONObject("data").getIntValue("access_token_expires_in");
                    agent.setAccessToken(token);
                    agent.setExpiresTime(Tools.addSeconds(new Date(), expiresIn - 600));
                    agent.setRefreshExpiresTime(Tools.addDay(new Date(), 30));
                    this.saveOrUpdate(agent);
                    result.put("errorcode", 0);
                    result.put("msg", "刷新成功");
                } else {
                    result.put("errorcode", 1);
                    result.put("msg", "刷新accessToken失败，请联系服务商");
                    result.put("data", json);
                }
            }else{
                result.put("errorcode", 1);
                result.put("msg", "服务器异常，请联系服务商");
            }
        }else{
            result.put("errorcode", 2);
            result.put("msg", "refreshAccessToken过期，请联系服务商");
        }
        return result;
    }

    public void getAdvertisersFromGdt(){
        String token = getAccessToken(1L);
        Map<String, String> params = new HashMap<String, String>();
        int totalPage = 1;
        JSONArray fields = new JSONArray();
        fields.add("account_id");
        fields.add("corporation_name");
        fields.add("system_status");
        params.put("fields", fields.toJSONString());
        params.put("page", "1");
        params.put("page_size", "100");
        JSONObject advs = GDTApiUtils.getAdvertiser(token, params);
        ListOperations<String,String> listOper = redisTemplate.opsForList();
        if(advs != null && advs.getIntValue("code") == 0){
            totalPage = advs.getJSONObject("data").getJSONObject("page_info").getIntValue("total_page");
            JSONArray array = advs.getJSONObject("data").getJSONArray("list");
            array.toJavaList(JSONObject.class).forEach(obj->{
                String accountId = obj.getString("account_id");
                String corporation_name = obj.getString("corporation_name");
                String system_status = obj.getString("system_status");
                if(system_status.equals("CUSTOMER_STATUS_NORMAL")&&!corporation_name.contains("正和智能")&&!corporation_name.contains("广东和邦")){
                    listOper.leftPush("advs", accountId+"-"+corporation_name);
                }

            });
        }
        for(int i = 2;i<=totalPage;i++){
            params.put("page", i+"");
            advs = GDTApiUtils.getAdvertiser(token, params);
            if(advs != null && advs.getIntValue("code") == 0){
                JSONArray array = advs.getJSONObject("data").getJSONArray("list");
                array.toJavaList(JSONObject.class).forEach(obj->{
                    String accountId = obj.getString("account_id");
                    String corporation_name = obj.getString("corporation_name");
                    String system_status = obj.getString("system_status");
                    if(system_status.equals("CUSTOMER_STATUS_NORMAL")&&!corporation_name.contains("正和智能")&&!corporation_name.contains("广东和邦")){
                        listOper.leftPush("advs", accountId+"-"+corporation_name);
                    }
                });
            }
        }
    }
    //2019-04-12~至今
    public void downLoadGdtOrder(String startDate, String endDate){
        new Thread(()->{
            getAdvertisersFromGdt();
            ListOperations<String,String> listOper = redisTemplate.opsForList();
            String token = getAccessToken(1L);
            Map<String, String> params = new HashMap<String, String>();
            JSONObject date_range = new JSONObject();
            date_range.put("start_date", startDate);
            date_range.put("end_date", endDate);
            params.put("date_range", date_range.toJSONString());
            JSONArray fields = new JSONArray();
            fields.add("ecommerce_order_id");
            fields.add("customized_page_name");
            fields.add("commodity_package_detail");
            fields.add("quantity");
            fields.add("price");
            fields.add("total_price");
            fields.add("user_name");
            fields.add("user_phone");
            fields.add("user_province");
            fields.add("user_city");
            fields.add("user_area");
            fields.add("user_address");
            fields.add("user_ip");
            fields.add("user_message");
            fields.add("destination_url");
            fields.add("ecommerce_order_time");
            params.put("fields", fields.toJSONString());
            while(listOper.size("advs") > 0){
                String value = listOper.leftPop("advs");
                String[] infos = value.split("-");
                String accountId = infos[0];
                String accountName = infos[1];
                params.put("account_id", accountId+"");
                params.put("page", "1");
                params.put("page_size", "100");
                JSONObject json = GDTApiUtils.getEcommerceOrder(token, params);
                if(json != null && json.getIntValue("code") == 0){
                    int totalPage = json.getJSONObject("data").getJSONObject("page_info").getIntValue("total_page");
                    if(totalPage > 0){
                        JSONArray list = json.getJSONObject("data").getJSONArray("list");
                        List<GdtOrder> orders = Lists.newArrayList();
                        list.toJavaList(JSONObject.class).forEach(obj->{
                            GdtOrder gdtOrder = new GdtOrder();
                            gdtOrder.setEcommerce_order_id(obj.getString("ecommerce_order_id"));
                            gdtOrder.setAccount_id(Integer.parseInt(accountId));
                            gdtOrder.setAccount_name(accountName);
                            gdtOrder.setCommodity_package_detail(obj.getString("commodity_package_detail"));
                            gdtOrder.setCustomized_page_name(obj.getString("customized_page_name"));
                            gdtOrder.setDestination_url(obj.getString("destination_url"));
                            gdtOrder.setEcommerce_order_time(obj.getString("ecommerce_order_time"));
                            gdtOrder.setPrice(obj.getIntValue("price"));
                            gdtOrder.setQuantity(obj.getIntValue("quantity"));
                            gdtOrder.setTotal_price(obj.getIntValue("total_price"));
                            gdtOrder.setUser_address(obj.getString("user_address"));
                            gdtOrder.setUser_area(obj.getString("user_area"));
                            gdtOrder.setUser_city(obj.getString("user_city"));
                            gdtOrder.setUser_ip(obj.getString("user_ip"));
                            gdtOrder.setUser_message(obj.getString("user_message"));
                            gdtOrder.setUser_name(obj.getString("user_name"));
                            gdtOrder.setUser_phone(obj.getString("user_phone"));
                            gdtOrder.setUser_province(obj.getString("user_province"));
                            orders.add(gdtOrder);
                        });
                        for(int i = 2; i <= totalPage;i++){
                            params.put("page", ""+i);
                            JSONObject json1 = GDTApiUtils.getEcommerceOrder(token, params);
                            if(json1 != null&&json1.getIntValue("code") == 0){
                                JSONArray list1 = json1.getJSONObject("data").getJSONArray("list");
                                list1.toJavaList(JSONObject.class).forEach(obj->{
                                    GdtOrder gdtOrder = new GdtOrder();
                                    gdtOrder.setEcommerce_order_id(obj.getString("ecommerce_order_id"));
                                    gdtOrder.setAccount_id(Integer.parseInt(accountId));
                                    gdtOrder.setAccount_name(accountName);
                                    gdtOrder.setCommodity_package_detail(obj.getString("commodity_package_detail"));
                                    gdtOrder.setCustomized_page_name(obj.getString("customized_page_name"));
                                    gdtOrder.setDestination_url(obj.getString("destination_url"));
                                    gdtOrder.setEcommerce_order_time(obj.getString("ecommerce_order_time"));
                                    gdtOrder.setPrice(obj.getIntValue("price"));
                                    gdtOrder.setQuantity(obj.getIntValue("quantity"));
                                    gdtOrder.setTotal_price(obj.getIntValue("total_price"));
                                    gdtOrder.setUser_address(obj.getString("user_address"));
                                    gdtOrder.setUser_area(obj.getString("user_area"));
                                    gdtOrder.setUser_city(obj.getString("user_city"));
                                    gdtOrder.setUser_ip(obj.getString("user_ip"));
                                    gdtOrder.setUser_message(obj.getString("user_message"));
                                    gdtOrder.setUser_name(obj.getString("user_name"));
                                    gdtOrder.setUser_phone(obj.getString("user_phone"));
                                    gdtOrder.setUser_province(obj.getString("user_province"));
                                    orders.add(gdtOrder);
                                });
                            }
                        }
                        if(!list.isEmpty()){
                            gdtOrderService.saveAll(orders);
                        }
                    }

                }
                try {
                    log.info("=================="+accountId+"====================");
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
