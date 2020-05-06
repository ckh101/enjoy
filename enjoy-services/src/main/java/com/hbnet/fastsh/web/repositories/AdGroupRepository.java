package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.AdGroup;

public interface AdGroupRepository extends BaseRepository<AdGroup, Long>  {
    AdGroup findByAdGroupId(Integer adGroupId);

    AdGroup findByAdGroupIdAndAccountId(Integer adGroupId, Integer accountId);

    AdGroup findByIdAndAccountId(Long id, Integer accountId);

    AdGroup findByPrivateCampaignId(Long privateCampaignId);
}
