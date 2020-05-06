package com.hbnet.fastsh.mongodb.repositories;

import com.hbnet.fastsh.mongodb.model.CampaignHourReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CampaignHourReportRepository extends MongoRepository<CampaignHourReport, String> {
}
