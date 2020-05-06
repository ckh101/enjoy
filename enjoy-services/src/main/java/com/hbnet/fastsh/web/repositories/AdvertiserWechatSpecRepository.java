package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.AdvertiserWechatSpec;
public interface AdvertiserWechatSpecRepository extends BaseRepository<AdvertiserWechatSpec, Long> {

    AdvertiserWechatSpec findFirstByAccountIdAndWechatAccountId(Integer accountId, String wechatAccountId);
    AdvertiserWechatSpec findFirstByWechatAccountId(String wechatAccountId);

}
