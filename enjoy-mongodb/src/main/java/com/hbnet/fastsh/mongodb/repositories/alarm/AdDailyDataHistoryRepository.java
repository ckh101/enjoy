package com.hbnet.fastsh.mongodb.repositories.alarm;

import com.hbnet.fastsh.mongodb.model.alarm.AdDailyDataHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdDailyDataHistoryRepository extends MongoRepository<AdDailyDataHistory, String> {
}
