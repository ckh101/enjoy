package com.hbnet.fastsh.mongodb.service.impl;

import com.hbnet.fastsh.mongodb.model.AdDailyReport;
import com.hbnet.fastsh.mongodb.service.base.AbstractSmartAdMongoService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


@Service
public class AdDailyReportService extends AbstractSmartAdMongoService<AdDailyReport> {

    public AdDailyReport findLastDailyReport(Integer adId){
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("ad_id").is(adId));
        Query query = new Query(criteria).with(new Sort(Sort.Direction.DESC, "date"));
        return findOne(query);
    }
}
