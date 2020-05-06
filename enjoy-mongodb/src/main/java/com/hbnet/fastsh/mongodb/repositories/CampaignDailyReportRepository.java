package com.hbnet.fastsh.mongodb.repositories;

import com.hbnet.fastsh.mongodb.model.CampaignDailyReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CampaignDailyReportRepository extends MongoRepository<CampaignDailyReport, String> {
}
