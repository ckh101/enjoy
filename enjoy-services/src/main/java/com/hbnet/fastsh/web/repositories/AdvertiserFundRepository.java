package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.AdvertiserFund;

import java.util.List;

public interface AdvertiserFundRepository extends BaseRepository<AdvertiserFund, Long> {

    AdvertiserFund findFirstByAccountIdAndFundType(Integer accountId, String fundType);
    List<AdvertiserFund> getFundsByAccountId(Integer accountId);
}
