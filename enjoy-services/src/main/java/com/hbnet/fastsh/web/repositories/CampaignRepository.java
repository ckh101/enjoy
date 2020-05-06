package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.Campaign;

import java.util.List;

public interface CampaignRepository extends BaseRepository<Campaign, Long> {
    Campaign findByCampaignId(Integer campaignId);
    Campaign findByCampaignIdAndAccountId(Integer campaignId, Integer accountId);
    Campaign findByIdAndAccountId(Long id, Integer accountId);

    List<Campaign> findByAccountIdAndConfiguredStatus(Integer accountId, String configuredStatus);
}
