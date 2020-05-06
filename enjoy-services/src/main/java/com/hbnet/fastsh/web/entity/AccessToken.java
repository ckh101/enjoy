package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="smartad_token")
@Setter
@Getter
public class AccessToken extends BaseEntity {

    @Column(name="appId")
	private String appId;

    @Column(name="token")
	private String token;

    @Column(name="expires_time")
	private Date expiresTime;

    @Column(name="refresh_token")
	private String refreshToken;

    @Column(name="refresh_expires_time")
	private Date refreshExpiresTime;
	

	
}
