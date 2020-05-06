package com.hbnet.fastsh.mongodb.repositories;

import com.hbnet.fastsh.mongodb.model.AdHourReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdHourReportRepository extends MongoRepository<AdHourReport, String> {
}
