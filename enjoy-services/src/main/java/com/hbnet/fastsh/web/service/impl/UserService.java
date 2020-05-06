package com.hbnet.fastsh.web.service.impl;

import com.google.common.collect.Lists;
import com.hbnet.fastsh.utils.Tools;
import com.hbnet.fastsh.web.entity.User;
import com.hbnet.fastsh.web.enums.OP;
import com.hbnet.fastsh.web.repositories.UserRepository;
import com.hbnet.fastsh.web.service.base.PageInfo;
import com.hbnet.fastsh.web.service.base.Predication;
import com.hbnet.fastsh.web.service.base.SpecificationFactory;
import com.hbnet.fastsh.web.service.page.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService extends CommonService {

    @Autowired
    UserRepository userRepository;

    public User findById(Long id){
        Optional<User> optional = userRepository.findById(id);
        return optional.isPresent()?optional.get():null;
    }

    public User saveOrUpdate(User user){
        return userRepository.saveAndFlush(user);
    }

	public void page(User user, PageInfo<User> pageInfo){
        List<Predication> ps = Lists.newArrayList();
        if(!Tools.isBlank(user.getAccount())) {
            ps.add(Predication.get(OP.LIKE, "account", "%" + user.getAccount() + "%"));
        }
        ps.add(Predication.get(OP.NOTEQ, "systemStatus", "DEL"));
        pageInfo.setData(userRepository.findAll(SpecificationFactory.where(ps), pageInfo.getLimit(new Sort(Sort.Direction.DESC, "createTime"))));
	}
	
	public boolean isExist(String account) {
		return userRepository.existsByAccount(account);
	}
	
	public boolean isExistPhone(String phone) {
		return userRepository.existsByPhone(phone);
	}
	
	public User findByPhone(String phone) {
		return userRepository.findFirstByPhone(phone);
	}

	public User findByAccount(String account){
	    return userRepository.findByAccount(account);
    }

    public List<User> findByIds(Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }

        return userRepository.findAllById(userIds);
    }

    public List<User> findAll() {
        return userRepository.findAll(CREATE_TIME_DESC);
    }

    @Override
    public JpaSpecificationExecutor getExcutor() {
        return userRepository;
    }
}
