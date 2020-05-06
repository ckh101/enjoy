package com.hbnet.fastsh.web.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="smartad_adgroup_targeting")
@Getter
@Setter
public class AdGroupTargeting extends  BaseEntity {


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

    @OneToOne(mappedBy = "targeting", fetch = FetchType.LAZY)
    @JsonIgnore
    AdGroup adGroup;

}
