package com.hbnet.fastsh.web.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName: PddUserStore
 * @Auther: zoulr@qq.com
 * @Date: 2019/9/24 12:00
 */
@Entity
@Table(name = PddUserStore.TB_NAME)
@Data
public class PddUserStore extends BaseEntity {
    public static final String TB_NAME = "smartad_pdd_user_store";
    private static final long serialVersionUID = -5353504217737737830L;

    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private String storeId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;
}