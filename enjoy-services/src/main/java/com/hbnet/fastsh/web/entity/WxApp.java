package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="smartad_wxapp")
@Setter
@Getter
public class WxApp extends BaseEntity {

	@Column(name="company")
	private String company;

    @Column(name="app_name")
	private String appName;

    @Column(name="app_id")
	private String appId;

    @Column(name="type_Id")
	private Integer typeId;

    @Column(name="auth_status")
	private Integer authStatus;

    @Column(name="access_token")
	private String accessToken;

    @Column(name="refresh_token")
	private String refreshToken;

    @Column(name="expires_time")
	private Date  expiresTime;

    @Column(name="func_info")
    private String funcInfo;

}
