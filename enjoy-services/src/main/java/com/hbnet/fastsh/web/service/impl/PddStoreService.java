package com.hbnet.fastsh.web.service.impl;

import com.google.common.collect.Lists;
import com.hbnet.fastsh.utils.ApiResponse;
import com.hbnet.fastsh.web.constant.PddStoreAccountType;
import com.hbnet.fastsh.web.entity.*;
import com.hbnet.fastsh.web.exception.ServiceException;
import com.hbnet.fastsh.web.repositories.PddFundStatementRepository;
import com.hbnet.fastsh.web.repositories.PddStoreRepository;
import com.hbnet.fastsh.web.repositories.PddUserStoreRepository;
import com.hbnet.fastsh.web.service.page.CommonService;
import com.hbnet.fastsh.web.service.page.SearchFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @ClassName: PddStoreService
 * @Auther: zoulr@qq.com
 * @Date: 2019/9/24 13:56
 * @Description: 拼多多店铺service类
 */
@Slf4j
@Service
public class PddStoreService extends CommonService {
    private static final String[] SEARCH_FIELD = new String[]{"storeId", "name", "userId"};

    @Autowired
    private PddStoreRepository pddStoreRepository;

    @Autowired
    private PddUserStoreRepository pddUserStoreRepository;

    @Autowired
    private PddAdProductService pddAdProductService;

    @Autowired
    private PddFundStatementRepository pddFundStatementRepository;

    @Autowired
    private UserService userService;

    public List<PddStore> selectByUserId(long userId) {
        return pddStoreRepository.selectByUserId(userId);
    }

    public PddStore selectByUserIdAndStoreId(long userId, String storeId) {
        PddUserStore condition = new PddUserStore();
        condition.setUserId(userId);
        condition.setStoreId(storeId);

        PddUserStore userStore = pddUserStoreRepository.findOne(Example.of(condition)).orElse(null);
        if (userStore == null) {
            return null;
        }

        return pddStoreRepository.findByStoreId(userStore.getStoreId());
    }

    public Page<PddStore> page(List<SearchFilter> searchFilter, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, CREATE_TIME_DESC);

        return page(PddStore.class, searchFilter, SEARCH_FIELD, pageable);
    }

    @Override
    public JpaSpecificationExecutor getExcutor() {
        return pddStoreRepository;
    }

    public PddStore save(PddStore param) {
        return pddStoreRepository.save(param);
    }

    public PddStore selectByStoreId(String storeId) {
        return pddStoreRepository.findByStoreId(storeId);
    }

    public List<PddStore> selectByStoreIds(Collection<String> storeIds) {
        return pddStoreRepository.findByStoreIdIn(storeIds);
    }

    /**
     * 指派店铺给用户
     * @param store
     * @param userIdList
     */
    public void assignStore(PddStore store, Set<Long> userIdList) {
        List<User> userList = userService.findByIds(userIdList);
        if (userList.isEmpty()) {
            pddUserStoreRepository.deleteByStoreId(store.getStoreId());
            return;
        }


        Date now = new Date();
        List<PddUserStore> saveList = Lists.newArrayList();
        for (User u : userList) {
            PddUserStore condition = new PddUserStore();
            condition.setUserId(u.getId());
            condition.setStoreId(store.getStoreId());

            if (pddUserStoreRepository.findOne(Example.of(condition)).isPresent()) {
                continue;
            }

            condition.setCreateTime(now);
            saveList.add(condition);
        }

        if (!saveList.isEmpty()) {
            pddUserStoreRepository.saveAll(saveList);
        }

        pddUserStoreRepository.deleteByStoreIdAndUserIdNotIn(store.getStoreId(), userIdList);
    }

    /**
     * 指派店铺给用户
     * @param store
     * @return
     */
    public List<PddUserStore> listUserStoreByStoreId(PddStore store) {
        PddUserStore condition = new PddUserStore();
        condition.setStoreId(store.getStoreId());

        return pddUserStoreRepository.findAll(Example.of(condition), CREATE_TIME_DESC);
    }

    /**
     * 冻结资金
     */
    @Transactional(rollbackFor = Throwable.class)
    protected void blockFund(PddStore store, PddAdProduct pddAdProduct, User operator) {
        Assert.isTrue(pddAdProduct.getAmount() > 0, "冻结金额需大于0");

        int ret = pddStoreRepository.blockBalance(store.getId(), pddAdProduct.getAmount());
        if (ret <= 0) {
            throw new ServiceException("操作失败，可用余额不足！");
        }

        String operatorVal = operator.getId() + "-" + operator.getUserName();

        PddFundStatement deduct = new PddFundStatement();
        deduct.setStoreId(store.getStoreId());
        deduct.setStoreName(store.getName());
        deduct.setType(1); // 扣除
        deduct.setAmount(pddAdProduct.getAmount());
        deduct.setAdProductId(pddAdProduct.getAdProductId());
        deduct.setAdProductName(pddAdProduct.getName());
        deduct.setAccountType(PddStoreAccountType.BALANCE.getCode());
        deduct.setRemark("投放产品[" + pddAdProduct.getName() + "]扣除可用余额");
        deduct.setOperator(operatorVal);
        deduct.setCreateTime(new Date());
        pddFundStatementRepository.save(deduct);

        PddFundStatement incr = new PddFundStatement();
        incr.setStoreId(store.getStoreId());
        incr.setStoreName(store.getName());
        incr.setType(2); // 注入
        incr.setAmount(pddAdProduct.getAmount());
        incr.setAdProductId(pddAdProduct.getAdProductId());
        incr.setAdProductName(pddAdProduct.getName());
        incr.setAccountType(PddStoreAccountType.BLOCKED_BALANCE.getCode());
        incr.setRemark("投放产品[" + pddAdProduct.getName() + "]转入冻结余额");
        incr.setOperator(operatorVal);
        incr.setCreateTime(new Date());
        pddFundStatementRepository.save(incr);
    }


    /**
     * 解冻资金
     */
    @Transactional(rollbackFor = Throwable.class)
    protected void unblockFund(PddStore store, PddAdProduct pddAdProduct, User operator) {
        Assert.isTrue(pddAdProduct.getAmount() > 0, "解冻金额需大于0");

        int ret = pddStoreRepository.unblockBalance(store.getId(), pddAdProduct.getAmount());
        if (ret <= 0) {
            throw new ServiceException("操作失败，冻结余额不足！");
        }

        String operatorVal = operator.getId() + "-" + operator.getUserName();

        PddFundStatement deduct = new PddFundStatement();
        deduct.setStoreId(store.getStoreId());
        deduct.setStoreName(store.getName());
        deduct.setType(1); // 扣除
        deduct.setAmount(pddAdProduct.getAmount());
        deduct.setAdProductId(pddAdProduct.getAdProductId());
        deduct.setAdProductName(pddAdProduct.getName());
        deduct.setAccountType(PddStoreAccountType.BLOCKED_BALANCE.getCode());
        deduct.setRemark("释放冻结余额，产品[" + pddAdProduct.getName() + "]");
        deduct.setOperator(operatorVal);
        deduct.setCreateTime(new Date());

        pddFundStatementRepository.save(deduct);

        PddFundStatement incr = new PddFundStatement();
        incr.setStoreId(store.getStoreId());
        incr.setStoreName(store.getName());
        incr.setType(2); // 注入
        incr.setAmount(pddAdProduct.getAmount());
        incr.setAdProductId(pddAdProduct.getAdProductId());
        incr.setAdProductName(pddAdProduct.getName());
        incr.setAccountType(PddStoreAccountType.BALANCE.getCode());
        incr.setRemark("增加可用余额，[" + pddAdProduct.getName() + "]");
        incr.setOperator(operatorVal);
        incr.setCreateTime(new Date());

        pddFundStatementRepository.save(incr);
    }

    /**
     * 设置金额投放产品
     * @param pddStore
     * @param product
     * @param amount
     * @param operator
     */
    @Transactional(rollbackFor = Throwable.class)
    public void place(PddStore pddStore, PddAdProduct product, long amount, User operator) {
        product.setAmount(amount);

        blockFund(pddStore, product, operator); // 冻结资金

        product.setStatus(2); // 待投放
        product.setUpdateTime(new Date());

        pddAdProductService.save(product);
    }

    /**
     * 撤销产品的投放状态
     * @param pddStore
     * @param product
     * @param operator
     */
    public void unPlace(PddStore pddStore, PddAdProduct product, User operator) {
        unblockFund(pddStore, product, operator); // 冻结资金

        product.setStatus(1); // 待投放
        product.setUpdateTime(new Date());

        pddAdProductService.save(product);
    }

    /**
     * 从冻结账户扣款
     * @param store
     * @param pddAdProduct
     * @param operator
     */
    public void withhold(PddStore store, PddAdProduct pddAdProduct, User operator) {
        String operatorVal = operator.getId() + "-" + operator.getUserName();

        PddFundStatement deduct = new PddFundStatement();
        deduct.setStoreId(store.getStoreId());
        deduct.setStoreName(store.getName());
        deduct.setType(1); // 扣除
        deduct.setAmount(pddAdProduct.getAmount());
        deduct.setAdProductId(pddAdProduct.getAdProductId());
        deduct.setAdProductName(pddAdProduct.getName());
        deduct.setAccountType(PddStoreAccountType.BLOCKED_BALANCE.getCode());
        deduct.setRemark("投放扣除冻结余额，产品[" + pddAdProduct.getName() + "]");
        deduct.setOperator(operatorVal);
        deduct.setCreateTime(new Date());

        pddFundStatementRepository.save(deduct);

        pddStoreRepository.withholdBlockBalance(store.getId(), pddAdProduct.getAmount());
    }

    /**
     * 充值
     * @param store 店铺
     * @param amount 充值金额
     * @param userRemark 说明
     * @param operator 操作人
     */
    @Transactional(rollbackFor = Throwable.class)
    public void deposit(PddStore store, long amount, String userRemark, User operator) {
        Assert.isTrue(amount > 0, "充值金额需大于0");

        int ret = pddStoreRepository.deposit(store.getId(), amount);
        if (ret <= 0) {
            throw new ServiceException("充值失败！请联系开发人员");
        }

        String remark = "充值";
        if (StringUtils.isNotBlank(userRemark)) {
            remark += "。\n备注" + userRemark;
        }

        PddFundStatement deduct = new PddFundStatement();
        deduct.setStoreId(store.getStoreId());
        deduct.setStoreName(store.getName());
        deduct.setType(2); // 注入
        deduct.setAmount(amount);
        deduct.setAccountType(PddStoreAccountType.BALANCE.getCode());
        deduct.setRemark(remark);
        deduct.setOperator(operator.getId() + "-" + operator.getUserName());
        deduct.setCreateTime(new Date());

        pddFundStatementRepository.save(deduct);
    }
}
