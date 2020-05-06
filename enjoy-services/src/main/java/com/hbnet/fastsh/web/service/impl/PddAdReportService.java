package com.hbnet.fastsh.web.service.impl;

import com.hbnet.fastsh.web.entity.PddAdReport;
import com.hbnet.fastsh.web.repositories.PddAdReportRepository;
import com.hbnet.fastsh.web.service.page.CommonService;
import com.hbnet.fastsh.web.service.page.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: PddAdReportService
 * @Auther: zoulr@qq.com
 * @Date: 2019/9/26 16:42
 * @Description: 拼多多广告报表数据service类
 */
@Service
public class PddAdReportService extends CommonService {
    private static final String[] SEARCH_FIELD = new String[]{"storeId", "name", "status", "statDate"};

    @Autowired
    private PddAdReportRepository pddAdReportRepository;

    public Page<PddAdReport> page(List<SearchFilter> searchFilter, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, CREATE_TIME_DESC);

        return page(PddAdReport.class, searchFilter, SEARCH_FIELD, pageable);
    }

    @Override
    public JpaSpecificationExecutor getExcutor() {
        return pddAdReportRepository;
    }

    public PddAdReport save(PddAdReport param) {
        return pddAdReportRepository.save(param);
    }

    public boolean checkExist(String adProductId, Date statDate) {
        PddAdReport condition = new PddAdReport();
        condition.setAdProductId(adProductId);
        condition.setStatDate(statDate);

        return pddAdReportRepository.findOne(Example.of(condition)).isPresent();
    }

    public PddAdReport selectById(Long id) {
        return pddAdReportRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        pddAdReportRepository.deleteById(id);
    }
}
