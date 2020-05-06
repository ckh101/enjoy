package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "smartad_user_industry")
@Getter
@Setter
public class UserIndustry implements Serializable {
    @Id
    private Long userId;
    @Id
    private Long industryId;
}
