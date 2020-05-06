package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="smartad_user")
@Setter
@Getter
@NamedEntityGraph(name="user.all", attributeNodes = {
        @NamedAttributeNode("role"),
        @NamedAttributeNode("operator"),
        @NamedAttributeNode("advertisers"),
        @NamedAttributeNode("wxApps"),
        @NamedAttributeNode("wxUser")
})
public class User extends BaseEntity{

    @Column(name="is_root")
	private int isRoot;

    @Column(name="account")
	private String account;

    @Column(name="user_name")
	private String userName;

    @Column(name="password")
	private String password;

    @Column(name="phone")
	private String phone;

    @Column(name="company")
	private String company;

    @Column(name="system_status")
	private String systemStatus;

    @Column(name="account_type")
	private String accountType;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="operator_id")
    @NotFound(action= NotFoundAction.IGNORE)
	private User operator;
	
	@Transient
	private Module modtree;
	
	@Transient
	private Set<String> permissions;

	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private WxUser wxUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name="smartad_user_role",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="role_id",referencedColumnName = "id"))
	private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="smartad_advertiser_user",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="advertiser_id",referencedColumnName = "id"))
    private List<Advertiser> advertisers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="smartad_wxapp_user",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="wx_app_id",referencedColumnName = "id"))
    private List<WxApp> wxApps;

	@Column(name="last_login_time")
    private Date lastLoginTime;
}
