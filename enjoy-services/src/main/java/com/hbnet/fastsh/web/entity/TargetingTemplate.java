package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="smart_targeting_template")
@Setter
@Getter
public class TargetingTemplate extends BaseEntity{

    private String templateName;

    private String age;

    private String gender;

    private String education;

    private String maritalStatus;

    private String workingStatus;

    private String geoLocation;

    private String userOs;

    private String newDevice;

    private String devicePrice;

    private String networkType;

    private String networkOperator;

    private String networkScene;

    private String dressingIndex;

    private String uvIndex;

    private String makeupIndex;

    private String climate;

    private String temperature;

    private String consumptionStatus;

    private String gamerConsumptionAbility;

    private String residentialCommunityPrice;

    private String wechatAdBehavior;

    private String customAudience;

    private String excludedCustomAudience;

    private String behaviorOrInterest;

    private String airQualityIndex;

    private String wechatOfficialAccountCategory;

    private String financialSituation;

    private String consumptionType;
}
