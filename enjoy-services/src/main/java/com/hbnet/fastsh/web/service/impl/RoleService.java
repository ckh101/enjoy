package com.hbnet.fastsh.web.service.impl;

import com.google.common.collect.Lists;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.Role;
import com.hbnet.fastsh.web.enums.OP;
import com.hbnet.fastsh.web.repositories.RoleRepository;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.base.Predication;
import com.hbnet.fastsh.web.service.base.SpecificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService{

    @Autowired
    RoleRepository roleRepository;

    public Role findById(Long id){
        Optional<Role> optional = roleRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }

    public void deletById(Long id){
        roleRepository.deleteById(id);
    }

    public List<Role> getAllList(){
        return roleRepository.findAll();
    }

    public Role saveOrUpdate(Role role){
        return roleRepository.saveAndFlush(role);
    }

	public void page(Role role, PageInfo<Role> pageInfo){
        List<Predication> ps = Lists.newArrayList();
        if(!Tools.isBlank(role.getRoleName())) {
            ps.add(Predication.get(OP.LIKE, "roleName", "%" + role.getRoleName() + "%"));
        }
        pageInfo.setData(roleRepository.findAll(SpecificationFactory.where(ps), pageInfo.getLimit(new Sort(Sort.Direction.DESC, "createTime"))));
	}
	
	public Role getRoleByRname(String rname){
		return roleRepository.findByRoleName(rname);
	}
}
