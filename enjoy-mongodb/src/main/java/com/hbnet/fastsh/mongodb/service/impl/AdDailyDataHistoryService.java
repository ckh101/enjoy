package com.hbnet.fastsh.mongodb.service.impl;

import com.hbnet.fastsh.mongodb.model.alarm.AdDailyDataHistory;
import com.hbnet.fastsh.mongodb.repositories.alarm.AdDailyDataHistoryRepository;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName: AdDailyDataHistoryService
 * @Auther: zoulr@qq.com
 * @Date: 2019/7/23 18:32
 */
@Service
public class AdDailyDataHistoryService {
    @Autowired
    private AdDailyDataHistoryRepository adDailyDataHistoryRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据广告组ID，结合历史拉取数据，计算历史ROI
     * @param adId
     * @param todayWebOrderAmount
     * @param todayCost
     * @return
     */
    public double calHistoryRoi(int adId, double todayWebOrderAmount, double todayCost) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.match(Criteria.where("ad_id").is(adId)));
        operations.add(Aggregation.group("ad_id")
                .sum("cost").as("costSum")
                .sum("web_order_amount").as("webOrderAmountSum"));

        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<BasicDBObject> ret = mongoTemplate.aggregate(aggregation, AdDailyDataHistory.CLT_NAME, BasicDBObject.class);

        Iterator<BasicDBObject> it = ret.iterator();
        while (it.hasNext()) {
            Document obj = (Document)((Object)it.next());
            todayCost += obj.getInteger("costSum", 0);
            todayWebOrderAmount += obj.getInteger("webOrderAmountSum", 0);
        }

        return todayCost == 0 ? 0 : todayWebOrderAmount / todayCost;
    }
}
