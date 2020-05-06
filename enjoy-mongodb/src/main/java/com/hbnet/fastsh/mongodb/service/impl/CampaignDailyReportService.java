package com.hbnet.fastsh.mongodb.service.impl;

import com.hbnet.fastsh.mongodb.model.CampaignDailyReport;
import com.hbnet.fastsh.mongodb.service.base.AbstractSmartAdMongoService;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


@Service
public class CampaignDailyReportService extends AbstractSmartAdMongoService<CampaignDailyReport> {
    public CampaignDailyReport findLastDailyReport(Integer campaignId){
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("campaignId").is(campaignId));
        Query query = new Query(criteria);
        return findOne(query);
    }
}
