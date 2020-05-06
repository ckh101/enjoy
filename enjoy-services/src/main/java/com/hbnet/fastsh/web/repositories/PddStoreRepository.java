package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.PddStore;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PddStoreRepository extends BaseRepository<PddStore,Long> {
    @Query("SELECT ps FROM PddStore ps INNER JOIN PddUserStore pus ON ps.storeId = pus.storeId WHERE pus.userId = ?1 ORDER BY ps.createTime DESC")
    List<PddStore> selectByUserId(long userId);

    PddStore findByStoreId(String storeId);

    List<PddStore> findByStoreIdIn(Collection<String> storeIds);

    /**
     * 冻结资金
     * @param id
     * @param amount
     * @return
     */
    @Transactional
    @Modifying
    @Query("UPDATE PddStore SET balance=balance-?2,blockedBalance=blockedBalance+?2 WHERE id=?1 AND balance>=?2")
    int blockBalance(Long id, long amount);

    /**
     * 解冻资金
     * @param id
     * @param amount
     * @return
     */
    @Transactional
    @Modifying
    @Query("UPDATE PddStore SET balance=balance+?2,blockedBalance=blockedBalance-?2 WHERE id=?1 AND blockedBalance>=?2")
    int unblockBalance(Long id, long amount);

    /**
     * 扣除资金
     * @param id
     * @param amount
     * @return
     */
    @Transactional
    @Modifying
    @Query("UPDATE PddStore SET blockedBalance=blockedBalance-?2 WHERE id=?1")
    int withholdBlockBalance(Long id, Long amount);

    /**
     * 充值可用余额
     * @param id
     * @param amount
     * @return
     */
    @Transactional
    @Modifying
    @Query("UPDATE PddStore SET balance=balance+?2 WHERE id=?1")
    int deposit(Long id, long amount);
}
