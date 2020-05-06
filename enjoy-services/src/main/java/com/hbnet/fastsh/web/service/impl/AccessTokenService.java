package com.hbnet.fastsh.web.service.impl;

import com.hbnet.fastsh.web.entity.AccessToken;
import com.hbnet.fastsh.web.repositories.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService {

    @Autowired
    AccessTokenRepository accessTokenRepository;

    public AccessToken saveOrUpdate(AccessToken token){
        return accessTokenRepository.save(token);
    }

	public AccessToken getByAppId(String appId) {
		return accessTokenRepository.findFirstByAppId(appId);
	}
}
