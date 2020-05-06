package com.hbnet.fastsh.web.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.Module;
import com.hbnet.fastsh.web.entity.Role;
import com.hbnet.fastsh.web.enums.OP;
import com.hbnet.fastsh.web.repositories.ModuleRepository;
import com.hbnet.fastsh.web.service.base.Predication;
import com.hbnet.fastsh.web.service.base.SpecificationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

@Service
public class ModuleService{
	@Autowired
    ModuleRepository moduleRepository;

    public Module findById(Long id){
        Optional<Module> optional = moduleRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }

    public Module saveOrUpdate(Module module){
        return moduleRepository.saveAndFlush(module);
    }
	public void getTree(long rid,int level, Module mod){
        Specification<Module> specification = (root, query, builder)->{
            List<Predicate> predicates = Lists.newArrayList();
            predicates.add(builder.equal(root.get("mstatus"), 1));
            Join<Module, Module> joinModule = root.join("parentModule", JoinType.INNER);
            predicates.add(builder.equal(joinModule.get("id"), mod.getId()));
            if(rid > 0){
                Join<Module, Role> joinRole = root.join("roles", JoinType.INNER);
                predicates.add(builder.equal(joinRole.get("id"), rid));
            }
            predicates.add(builder.gt(root.get("level"), 0));
            predicates.add(builder.le(root.get("level"), level));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        List<Module> modules = moduleRepository.findAll(specification, new Sort(Sort.Direction.ASC, "seq"));
        mod.setChildren(modules);


		/*SqlBuilder sql = new SqlBuilder();
		sql.appendSql("select id from gl_module where mstatus = 1 and pid = ");
		sql.appendValue(mod.getId());
		sql.appendSql(" and level > 0 and level <= ");
		sql.appendValue(level);
		if(rid > 0){
			sql.appendSql(" and  id in (select mid from gl_role_module where rid = ");
			sql.appendValue(rid);
			sql.appendSql(")");
		}
		sql.appendSql(" order by seq asc");
		List<Module> mods = this.list(Module.class, sql.getSql(), sql.getValues());
		mod.setChildren(mods);
		if(!Tools.isBlank(mods)&& mods.size() > 0){
			for(Module m:mods){
				if(m.getLevel() < level){
					getTree(rid, level, m);
				}else{
					break;
				}
			}
		}*/
	}
	public List<Module> getlist(Module mod){
        List<Predication> ps =Lists.newArrayList();
        if(!Tools.isBlank(mod.getMname())){
            ps.add(Predication.get(OP.LIKE, "mname", "%"+mod.getMname()+"%"));
        }
	    List<Module> modules = moduleRepository.findAll(SpecificationFactory.where(ps));
		return modules;
	}
	public Module findByName(String mname){
		return moduleRepository.findFirstByMname(mname);
	}

	public List<Module> getPermissionsByRole(Long rId){
        Specification<Module> specification = (root, query, builder)->{
            List<Predicate> predicates = Lists.newArrayList();
            predicates.add(builder.equal(root.get("mstatus"), 1));
            Join<Module, Role> joinRole = root.join("roles", JoinType.INNER);
            predicates.add(builder.equal(joinRole.get("id"), rId));
            predicates.add(builder.le(root.get("level"), 3));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        List<Module> modules = moduleRepository.findAll(specification);
        return modules;
    }

	public void deleteById(Long id){
        moduleRepository.deleteById(id);
    }

	public List<Module> getListByLevelLt(Integer level){
        return moduleRepository.findAllByLevelLessThan(level);
    }
	public  void toZtreeData(Module module,List<JSONObject> treeData, List<Long> mids) {
		JSONObject data = new JSONObject();
		data.put("id", module.getId());
        data.put("name", module.getMname());
        data.put("pId", module.getParentModule()==null?1:module.getParentModule().getId());
        data.put("open", true);
        if(mids != null) {
        	if(mids.contains(module.getId())) {
        		data.put("checked", true);
        	}
        }
        treeData.add(data);
		if(module.getChildren() != null && !module.getChildren().isEmpty()) {
	        for(Module m:module.getChildren()) {
	        	toZtreeData(m,treeData,mids);
	        }
		}
	}
}
