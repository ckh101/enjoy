package com.hbnet.fastsh.web.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName: PddStore
 * @Auther: zoulr@qq.com
 * @Date: 2019/9/24 11:55
 */
@Entity
@Table(name = PddStore.TB_NAME)
@Data
public class PddStore extends BaseEntity {
    public static final String TB_NAME = "smartad_pdd_store";

    private static final long serialVersionUID = 418022902570267868L;

    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private String storeId;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 店铺描述
     */
    private String detail;

    /**
     * 可用余额
     */
    private Long balance;

    /**
     * 冻结余额
     */
    @Column(name = "blocked_balance")
    private Long blockedBalance;
}