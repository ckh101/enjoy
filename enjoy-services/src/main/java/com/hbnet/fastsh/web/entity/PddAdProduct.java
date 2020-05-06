package com.hbnet.fastsh.web.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
*  拼多多店铺广告产品
*/
@Entity
@Table(name = PddAdProduct.TB_NAME)
@Data
public class PddAdProduct extends BaseEntity {
    private static final long serialVersionUID = -8019030845925280150L;
    public static final String TB_NAME = "smartad_pdd_ad_product";

    /**
    * 广告产品id
    */
    @Column(name = "ad_product_id")
    private String adProductId;

    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private String storeId;

    /**
    * 产品名称
    */
    private String name;

    /**
    * 投放链接
    */
    @Column(name = "target_url")
    private String targetUrl;

    /**
    * 人群包链接
    */
    @Column(name = "crowd_pack_url")
    private String crowdPackUrl;

    /**
    * 投放金额
    */
    private Long amount;

    /**
    * 状态。1待提交，2待投放，3投放中，4投放完成
    */
    private Integer status;

    /**
    * 备注
    */
    private String remark;
}