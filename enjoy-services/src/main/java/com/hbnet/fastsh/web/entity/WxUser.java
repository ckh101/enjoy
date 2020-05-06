package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="smartad_wxuser")
@Setter
@Getter
public class WxUser extends BaseEntity {

    @Column(name="open_id")
	private String openId;

    @Column(name="union_id")
	private String unionId;

    @Column(name="nick_name")
	private String nickName;

    @Column(name="head_url")
	private String headUrl;

    @Column(name="sex")
	private Integer sex;

    @Column(name="phone")
	private String phone;

    @Column(name="token")
	private String token;

    @Column(name="refresh_token")
	private String refreshToken;

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
	private User user;
	
}
