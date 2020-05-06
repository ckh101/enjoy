package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.PddAdProduct;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PddAdProductRepository extends BaseRepository<PddAdProduct,Long> {

    PddAdProduct findFirstByAdProductId(String adProductId);

    @Query("SELECT status AS stats,COUNT(id) AS cnt FROM PddAdProduct WHERE storeId=?1 GROUP BY status")
    List<Object[]> countGroupByStatus(String storeId);

    @Query("FROM PddAdProduct WHERE storeId=?1 AND status IN(?2) AND updateTime>=?3")
    List<PddAdProduct> findRecentlyPlace(String storeId, List<Integer> statusList, Date aMonthAgo);
}
