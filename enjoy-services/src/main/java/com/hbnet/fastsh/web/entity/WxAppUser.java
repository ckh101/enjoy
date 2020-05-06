package com.hbnet.fastsh.web.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="smartad_wxapp_user")
public class WxAppUser implements Serializable {

    private static final long serialVersionUID = 8733117343487389937L;
    @Id
    @Column(name="wx_app_id")
    private Long wxAppId;

    @Id
    @Column(name="user_id")
    private Long userId;


}
