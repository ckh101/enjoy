package com.hbnet.fastsh.web.service.impl;

import com.google.common.collect.Lists;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.WxApp;
import com.hbnet.fastsh.web.enums.OP;
import com.hbnet.fastsh.web.repositories.WxAppRepository;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.base.Predication;
import com.hbnet.fastsh.web.service.base.SpecificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WxAppService{

    @Autowired
    WxAppRepository wxAppRepository;

    public WxApp findById(Long id){
        Optional<WxApp> optional = wxAppRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }

    public WxApp saveOrUpate(WxApp app){
        return wxAppRepository.saveAndFlush(app);
    }

	public void page(WxApp app, PageInfo<WxApp> pageInfo){

	    List<Predication> ps = Lists.newArrayList();
        if(!Tools.isBlank(app.getCompany())){
            ps.add(Predication.get(OP.LIKE,"company", "%"+ app.getCompany() + "%"));
        }

        if(!Tools.isBlank(app.getAppName())){
            ps.add(Predication.get(OP.LIKE,"appName","%"+ app.getCompany() + "%"));
        }
         pageInfo.setData(wxAppRepository.findAll(SpecificationFactory.where(ps),pageInfo.getLimit(new Sort(Sort.Direction.DESC,"updateTime"))));
	}
	
	public boolean isExist(String appName) {
		return wxAppRepository.existsByAppName(appName);
	}
	
	public WxApp getByAppId(String appId) {
		return wxAppRepository.findByAppId(appId);
	}

	public List<WxApp> getList(){
		return wxAppRepository.findAll();
	}

	public List<WxApp> findByIdIn(Long[] ids){
        return wxAppRepository.findByIdIn(ids);
    }
}
