package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="smartad_wechat_offical_account_category")
@Getter
@Setter
public class WechatOfficialAccountCategory{

    @Id
    private Long id;

    private String name;

    private String parentName;

    private Long parentId;
}
