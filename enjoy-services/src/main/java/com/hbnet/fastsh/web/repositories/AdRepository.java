package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.Ad;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdRepository extends BaseRepository<Ad, Long> {
    Ad findByIdAndAccountId(Long id, Integer accountId);

    Ad findByAdIdAndAccountId(int adId, int accountId);

    Ad findByAdId(Integer adId);

    @Modifying
    @Query(value="update smartad_ad set isDeleted = true where campaignId = ?1", nativeQuery = true)
    int deleteByCampaignId(Long campaignId);

    @Query(value="select * from smartad_ad where adCreativeId = ?1", nativeQuery = true)
    Ad findByAdCreativeId(Long adcreativeId);

    @Query("SELECT id FROM Ad WHERE accountId=?1 AND id IN(?2)")
    List<Long> findIdByAccountIdAndIdIn(int accountId, List<Long> ids);
}
