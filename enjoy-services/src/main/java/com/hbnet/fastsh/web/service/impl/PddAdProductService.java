package com.hbnet.fastsh.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.hbnet.fastsh.web.constant.PddAdProductStatus;
import com.hbnet.fastsh.web.entity.*;
import com.hbnet.fastsh.web.exception.ServiceException;
import com.hbnet.fastsh.web.repositories.PddAdProductRepository;
import com.hbnet.fastsh.web.service.page.CommonService;
import com.hbnet.fastsh.web.service.page.SearchFilter;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.hbnet.fastsh.web.constant.PddAdProductStatus.COMPLETE;
import static com.hbnet.fastsh.web.constant.PddAdProductStatus.PLACING;

/**
 * @ClassName: PddAdProductService
 * @Auther: zoulr@qq.com
 * @Date: 2019/9/26 12:00
 * @Description: 拼多多店铺产品service类
 */
@Service
public class PddAdProductService extends CommonService {
    private static final String[] SEARCH_FIELD = new String[]{"adProductId", "storeId", "name", "status"};

    @Autowired
    private PddAdProductRepository pddAdProductRepository;

    @Autowired
    private PddStoreService pddStoreService;

    public Page<PddAdProduct> page(List<SearchFilter> searchFilter, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, CREATE_TIME_DESC);

        return page(PddAdProduct.class, searchFilter, SEARCH_FIELD, pageable);
    }

    @Override
    public JpaSpecificationExecutor getExcutor() {
        return pddAdProductRepository;
    }

    public PddAdProduct save(PddAdProduct param) {
        return pddAdProductRepository.save(param);
    }

    public PddAdProduct selectByAdProductId(String adProductId) {
        return pddAdProductRepository.findFirstByAdProductId(adProductId);
    }

    /**
     * 更新投放状态
     * @param record
     * @param newStatus
     * @param operator
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updatePlaceStatus(PddAdProduct record, PddAdProductStatus newStatus, User operator) {
        if (record.getStatus().equals(newStatus.getCode())) {
            return;
        }

        PddAdProductStatus recordStatus = PddAdProductStatus.getByCode(record.getStatus());
        if (recordStatus != PddAdProductStatus.WAIT_PLACE && recordStatus != PLACING) {
            throw new ServiceException("仅允许设置待投放和投放中的产品！");
        }

        if (newStatus != PLACING && newStatus != PddAdProductStatus.COMPLETE) {
            throw new ServiceException("仅允许设置为投放中或完成投放！");
        }

        record.setStatus(newStatus.getCode());
        record.setUpdateTime(new Date());

        pddAdProductRepository.save(record);

        if (newStatus == PddAdProductStatus.COMPLETE) {
            // 投放完成，资金从冻结账户扣除
            pddStoreService.withhold(pddStoreService.selectByStoreId(record.getStoreId()), record, operator);
        }
    }

    public Map<Integer, Long> countGroupByStatus(String storeId) {
        List<Object[]> list = pddAdProductRepository.countGroupByStatus(storeId);

        Map<Integer, Long> map = Maps.newHashMap();
        for (Object[] item : list) {
            map.put((Integer)item[0], (Long)item[1]);
        }

        for (PddAdProductStatus sts : PddAdProductStatus.values()) {
            if (!map.containsKey(sts.getCode())) {
                map.put(sts.getCode(), 0L);
            }
        }

        return map;
    }

    public List<PddAdProduct> recentlyPlace(PddStore store) {
        Date aMonthAgo = DateUtils.addDays(new Date(), -30);
        return pddAdProductRepository.findRecentlyPlace(store.getStoreId(), Arrays.asList(PLACING.getCode(), COMPLETE.getCode()), aMonthAgo);
    }
}
