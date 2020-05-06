package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.AccessToken;

public interface AccessTokenRepository extends  BaseRepository<AccessToken, Long> {

    AccessToken findFirstByAppId(String appId);
}
