package com.hbnet.fastsh.mongodb.service.impl;

import com.hbnet.fastsh.mongodb.model.GdtOrder;
import com.hbnet.fastsh.mongodb.service.base.AbstractSmartAdMongoService;
import com.hbnet.fastsh.mongodb.service.base.PageInfo;
import com.hbnet.fastsh.utils.Tools;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GdtOrderService extends AbstractSmartAdMongoService<GdtOrder> {

    public List<GdtOrder> findByDate(PageInfo<GdtOrder> page, String startDate, String endDate, Map<String, String> params){
        Criteria criteria = Criteria.where("ecommerce_order_time").gte(startDate).lte(endDate);
        if(params.containsKey("pro")&&!Tools.isBlank(params.get("pro"))){
            criteria.and("customized_page_name").regex(params.get("pro"));
        }

        if(params.containsKey("accountName")&&!Tools.isBlank(params.get("accountName"))){
            criteria.and("account_name").regex(params.get("accountName"));
        }

        if(params.containsKey("accountId")&&!Tools.isBlank(params.get("accountId"))){
            criteria.and("account_id").is(Tools.intValue(params.get("accountId"),0));
        }
        if(page != null){
            findPage(page, Query.query(criteria), new Sort(Sort.Direction.DESC, "ecommerce_order_time"));
            return null;
        }else{
            return find(Query.query(criteria).with(new Sort(Sort.Direction.DESC, "ecommerce_order_time")));
        }
    }
}
