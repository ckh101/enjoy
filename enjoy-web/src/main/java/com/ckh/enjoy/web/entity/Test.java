package com.ckh.enjoy.web.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Test")
@Data
public class Test extends BaseEntity {
    private static final long serialVersionUID = 3381156010360720145L;
    private String name;
}
