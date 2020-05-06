package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="smartad_gdt_behavior")
@Getter
@Setter
public class GdtBehavior extends BaseEntity {

    private String name;

    private String parentName;

    private Long parentId;
}
