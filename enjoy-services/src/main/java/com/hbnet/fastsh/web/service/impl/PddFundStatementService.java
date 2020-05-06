package com.hbnet.fastsh.web.service.impl;

import com.hbnet.fastsh.web.entity.PddFundStatement;
import com.hbnet.fastsh.web.repositories.PddFundStatementRepository;
import com.hbnet.fastsh.web.service.page.CommonService;
import com.hbnet.fastsh.web.service.page.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: PddFundStatementService
 * @Auther: zoulr@qq.com
 * @Date: 2019/9/26 10:05
 * @Description: 资金流水service
 */
@Service
public class PddFundStatementService extends CommonService {
    private static final String[] SEARCH_FIELD = new String[]{"adProductId", "adProductName", "storeId", "name", "type", "accountType"};
    protected static final Sort DEFAULT_ORDER_BY = new Sort(Sort.Direction.DESC, "createTime", "id");

    @Autowired
    private PddFundStatementRepository pddFundStatementRepository;

    public PddFundStatement save(PddFundStatement statement) {
        return pddFundStatementRepository.save(statement);
    }

    public Page<PddFundStatement> page(List<SearchFilter> searchFilter, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, DEFAULT_ORDER_BY);

        return page(PddFundStatement.class, searchFilter, SEARCH_FIELD, pageable);
    }

    @Override
    public JpaSpecificationExecutor getExcutor() {
        return pddFundStatementRepository;
    }
}
