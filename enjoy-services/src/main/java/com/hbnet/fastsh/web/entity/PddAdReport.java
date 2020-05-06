package com.hbnet.fastsh.web.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
*  拼多多产品消耗统计
*/
@Entity
@Table(name = PddAdReport.TB_NAME)
@Data
public class PddAdReport extends BaseEntity {
    public static final String TB_NAME = "smartad_pdd_ad_report";
    private static final long serialVersionUID = -6295586427771827781L;

    /**
    * 广告产品id
    */
    @Column(name = "ad_product_id")
    private String adProductId;

    /**
    * 产品名称
    */
    @Column(name = "ad_product_name")
    private String adProductName;

    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private String storeId;

    /**
    * 展示数
    */
    @Column(name = "display_cnt")
    private Integer displayCnt;

    /**
    * 点击数
    */
    @Column(name = "click_cnt")
    private Integer clickCnt;

    /**
    * 消耗金额
    */
    private Long cost;

    /**
    * 统计日期
    */
    @Column(name = "stat_date")
    private Date statDate;

}