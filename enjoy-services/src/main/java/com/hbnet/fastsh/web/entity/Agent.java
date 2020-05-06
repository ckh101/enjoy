package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "smartad_agent")
@Getter
@Setter
public class Agent extends  BaseEntity {

    private String accountId;

    private String accessToken;

    private Date expiresTime;

    private String refreshAccessToken;

    private Date refreshExpiresTime;

    private String callbackUrl;

}
