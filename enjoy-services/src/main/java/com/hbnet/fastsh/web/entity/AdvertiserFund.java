package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "smartad_advertiser_fund")
@Getter
@Setter
public class AdvertiserFund extends BaseEntity{

    private int accountId;

    private String fundType;

    private int balance;

    private String fundStatus;

    private int realtimeCost;

}
