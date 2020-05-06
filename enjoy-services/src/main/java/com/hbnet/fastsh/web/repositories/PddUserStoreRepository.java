package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.PddUserStore;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PddUserStoreRepository extends BaseRepository<PddUserStore,Long> {
    @Transactional
    @Modifying
    int deleteByUserIdAndStoreIdIn(long userId, List<String> storeId);

    @Transactional
    @Modifying
    int deleteByStoreId(String storeId);

    @Transactional
    @Modifying
    int deleteByStoreIdAndUserIdNotIn(String storeId, Collection<Long> userIdList);
}
