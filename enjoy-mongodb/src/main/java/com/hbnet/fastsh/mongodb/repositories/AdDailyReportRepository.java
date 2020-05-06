package com.hbnet.fastsh.mongodb.repositories;

import com.hbnet.fastsh.mongodb.model.AdDailyReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdDailyReportRepository extends MongoRepository<AdDailyReport, String> {
}
