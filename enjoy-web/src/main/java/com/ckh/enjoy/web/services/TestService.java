package com.ckh.enjoy.web.services;

import com.ckh.enjoy.web.dao.TestDao;
import com.ckh.enjoy.web.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestService {
    @Autowired
    private TestDao testDao;

    public Test save(Test test){
        return testDao.saveAndFlush(test);
    }

}
