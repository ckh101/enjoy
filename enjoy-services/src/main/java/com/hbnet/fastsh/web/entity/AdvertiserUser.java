package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="smartad_advertiser_user")
@Getter
@Setter
public class AdvertiserUser implements Serializable {


    @Id
    @Column(name="user_id")
    private long userId;

    @Id
    @Column(name="advertiser_id")
    private long advertiserId;


}
