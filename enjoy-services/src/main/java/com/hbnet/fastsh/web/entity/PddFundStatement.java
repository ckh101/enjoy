package com.hbnet.fastsh.web.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = PddFundStatement.TB_NAME)
@Data
public class PddFundStatement extends BaseEntity {
    private static final long serialVersionUID = 4198190784682861755L;
    public static final String TB_NAME = "smartad_pdd_fund_statement";

    /**
    * 店铺id
    */
    @Column(name = "store_id")
    private String storeId;

    /**
     * 产品id
     */
    @Column(name = "ad_product_id")
    private String adProductId;

    /**
     * 产品名称
     */
    @Column(name = "ad_product_name")
    private String adProductName;

    /**
    * 店铺名称
    */
    @Column(name = "store_name")
    private String storeName;

    /**
    * 操作类型，1扣除，2注入
    */
    private Integer type;

    /**
    * 操作账户:1 余额账户，2 冻结账户
    */
    @Column(name = "account_type")
    private Integer accountType;

    /**
    * 金额
    */
    private Long amount;

    /**
     * 操作人
     */
    private String operator;

    /**
    * 备注
    */
    private String remark;
}