package com.hbnet.fastsh.web.service.impl;

import com.google.common.collect.Lists;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.Industry;
import com.hbnet.fastsh.web.repositories.IndustryRepository;
import com.hbnet.fastsh.web.service.base.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

@Service
public class IndustryService{

    @Autowired
    IndustryRepository industryRepository;

    public Industry findById(Long id){
        Optional<Industry> optional = industryRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }

    public Industry saveOrUpdate(Industry industry){
        return industryRepository.saveAndFlush(industry);
    }

    public void page(Industry industry, PageInfo<Industry> pageInfo){
        Specification<Industry> specification = (root, query, builder)->{
            List<Predicate> predicates = Lists.newArrayList();
            if(!Tools.isBlank(industry.getIndustry())){
                predicates.add(builder.like(root.get("industry"), "%"+industry.getIndustry()+"%"));
            }

            Join<Industry, Industry> joinIndustry = root.join("parent", JoinType.INNER);
            if(industry.getParent() != null && industry.getParent().getId() > 0){
                predicates.add(builder.equal(joinIndustry.get("id"), industry.getParent().getId()));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        Page<Industry> industrys = industryRepository.findAll(specification, pageInfo.getLimit(new Sort(Sort.Direction.ASC, "updateTime")));
        pageInfo.setData(industrys);

    }

    public List<Industry> getAllParentList(){
        return industryRepository.getAllParentList();
    }

    public Boolean isExist(String industry) {
        return industryRepository.existsByIndustry(industry);
    }

    public List<Industry> getAllChildrenList(){
        return industryRepository.getAllChildrenList();
    }


    public void deleteById(Long id){
        industryRepository.deleteById(id);
    }
}
