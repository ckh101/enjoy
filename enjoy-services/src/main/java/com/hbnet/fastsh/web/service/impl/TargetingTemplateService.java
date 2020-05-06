package com.hbnet.fastsh.web.service.impl;

import com.google.common.collect.Lists;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.TargetingTemplate;
import com.hbnet.fastsh.web.enums.OP;
import com.hbnet.fastsh.web.repositories.TargetingTemplateRepository;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.base.Predication;
import com.hbnet.fastsh.web.service.base.SpecificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TargetingTemplateService {

    @Autowired
    TargetingTemplateRepository targetingTemplateRepository;

    public void page(TargetingTemplate targetingTemplate, PageInfo<TargetingTemplate> page){
        List<Predication> ps = Lists.newArrayList();
        if(!Tools.isBlank(targetingTemplate.getTemplateName())){
            ps.add(Predication.get(OP.LIKE, "tempalteName", "%"+targetingTemplate.getTemplateName()+"%"));
        }
        page.setData(targetingTemplateRepository.findAll(SpecificationFactory.where(ps),page.getLimit(new Sort(Sort.Direction.DESC, "updateTime"))));
    }
    public TargetingTemplate saveOrUpdate(TargetingTemplate targetingTemplate){
        return targetingTemplateRepository.saveAndFlush(targetingTemplate);
    }

    public TargetingTemplate findById(Long id){
        Optional<TargetingTemplate> optional = targetingTemplateRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }

    public void delById(Long id){
        targetingTemplateRepository.deleteById(id);
    }

    public List<com.hbnet.fastsh.web.domain.TargetingTemplate> findList(){
        List<com.hbnet.fastsh.web.domain.TargetingTemplate> list = targetingTemplateRepository.findList();
        return list;
    }
}
