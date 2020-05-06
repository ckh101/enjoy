package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "smarad_gdt_interest")
@Getter
@Setter
public class GdtInterest extends BaseEntity {

    private String name;

    private Long parentId;

    private String parentName;
}
