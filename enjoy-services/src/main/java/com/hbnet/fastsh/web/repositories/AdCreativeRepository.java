package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.AdCreative;

public interface AdCreativeRepository extends BaseRepository<AdCreative,Long>  {
    AdCreative findByAdCreativeIdAndAccountId(Integer adcreativeId, Integer accountId);

    AdCreative findByIdAndAccountId(Long id, Integer accountId);

    AdCreative findByPrivateCampaignId(Long privateCampaignId);
}
