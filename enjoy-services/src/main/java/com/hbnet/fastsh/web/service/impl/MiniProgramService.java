package com.hbnet.fastsh.web.service.impl;

import com.google.common.collect.Lists;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.MiniProgram;
import com.hbnet.fastsh.web.enums.OP;
import com.hbnet.fastsh.web.repositories.MiniProgramRepository;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.base.Predication;
import com.hbnet.fastsh.web.service.base.SpecificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MiniProgramService {

    @Autowired
    MiniProgramRepository miniProgramRepository;

    public MiniProgram savaOrUpdate(MiniProgram miniProgram){
        return miniProgramRepository.saveAndFlush(miniProgram);
    }

    public MiniProgram findById(Long id){
        Optional<MiniProgram> optional = miniProgramRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }

    public void delById(Long id){
        miniProgramRepository.deleteById(id);
    }

    public void page(MiniProgram miniProgram, PageInfo<MiniProgram> page){
        List<Predication> ps = Lists.newArrayList();
        if(!Tools.isBlank(miniProgram.getMiniProgramName())){
            ps.add(Predication.get(OP.LIKE, "miniProgramName", "%"+miniProgram.getMiniProgramName()+"%"));
        }
        page.setData(miniProgramRepository.findAll(SpecificationFactory.where(ps), page.getLimit(new Sort(Sort.Direction.DESC, "updateTime"))));
    }

    public List<MiniProgram> findAll(){
        return miniProgramRepository.findAll();
    }


}
