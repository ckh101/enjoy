package com.hbnet.fastsh.web.service.impl;

import com.hbnet.fastsh.web.entity.WxUser;
import com.hbnet.fastsh.web.repositories.WxUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxUserService{

    @Autowired
    WxUserRepository wxUserRepository;

    public WxUser saveOrUpdate(WxUser wxUser){
        return wxUserRepository.saveAndFlush(wxUser);
    }

	public WxUser findByOpenId(String openId) {
		return wxUserRepository.findByOpenId(openId);
	}

	public WxUser findWxUserByPhone(String phone){
	    return wxUserRepository.findByPhone(phone);
    }
	

}
