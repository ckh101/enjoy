package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="smartad_advertiser")
@Setter
@Getter
public class Advertiser extends BaseEntity{

    @Column(name="accountId")
    private Integer accountId;

    @Column(name="agentId")
    private Long agentId;

    @Column(name="dailyBudget")
    private Integer dailyBudget;

    @Column(name="systemStatus")
    private String systemStatus;

    @Column(name="rejectMessage")
    private String rejectMessage;

    @Column(name="corporationName")
    private String corporationName;

    @Column(name="corporationLicence")
    private String corporationLicence;

    @Column(name="certificationImage")
    private String certificationImage;

    @Column(name="identityNumber")
    private String identityNumber;

    @Column(name="corporateImageName")
    private String corporateImageName;

    @Column(name="corporateImageLogo")
    private String corporateImageLogo;

    @Column(name="systemIndustryId")
    private Integer systemIndustryId;

    @Column(name="customizedIndustry")
    private String customizedIndustry;

    @Column(name="introductionUrl")
    private String introductionUrl;

    @Column(name="contactPerson")
    private String contactPerson;

    @Column(name="contactPersonEmail")
    private String contactPersonEmail;

    @Column(name="contactPersonTelephone")
    private String contactPersonTelephone;

    @Column(name="contactPersonMobile")
    private String contactPersonMobile;

    @Column(name="wxOfficialAccount")
    private String wxOfficialAccount;

    @Column(name="wxAppId")
    private String wxAppId;

    @Column(name="originalId")
    private String originalId;

    @Column(name="authStatus")
    private Integer authStatus;

    @Column(name="wxAuthStatus")
    private Integer wxAuthStatus;

    @Column(name="accessToken")
    private String accessToken;

    @Column(name="expiresTime")
    private Date expiresTime;

    @Column(name="refreshAccessToken")
    private String refreshAccessToken;

    @Column(name="refreshExpiresTime")
    private Date refreshExpiresTime;

    @Column(name="webUserActionSetId")
    private Integer webUserActionSetId;

    @ManyToMany
    @JoinTable(name="smartad_advertiser_user",joinColumns = @JoinColumn(name="advertiser_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"))
    private List<User> users;

}
