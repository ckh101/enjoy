package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "smartad_advertiser_wechat_spec")
@Getter
@Setter
public class AdvertiserWechatSpec extends BaseEntity {

    private Integer accountId;

    private String wechatAccountId;

    private String wechatAccountName;

    private String systemStatus;

    private Integer systemIndustryId;

    private String industryName;

    private String contactPerson;

    private String contactPersonTelephone;

    private String businessType;

    private String businessContent;

    private String rejectMessage;

    private Date createTime;

    private Date updateTime;
}

