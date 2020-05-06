package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="smart_miniprogram")
@Setter
@Getter
public class MiniProgram extends BaseEntity {

    private String miniProgramName;

    private String miniProgramId;

}
