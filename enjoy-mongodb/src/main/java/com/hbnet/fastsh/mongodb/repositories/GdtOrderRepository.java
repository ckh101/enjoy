package com.hbnet.fastsh.mongodb.repositories;

import com.hbnet.fastsh.mongodb.model.GdtOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GdtOrderRepository extends MongoRepository<GdtOrder, String> {
}
