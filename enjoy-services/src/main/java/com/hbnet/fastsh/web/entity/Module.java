package com.hbnet.fastsh.web.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="smartad_module")
@Setter
@Getter
public class Module extends  BaseEntity{

    @Column(name="mname")
    private String mname;

    @Column(name="level")
    private Integer level;

    @Column(name="url")
    private String url;

    @Column(name="menuIcon")
    private String menuicon;

    @Column(name="mtype")
    private Integer mtype;

    @Column(name="seq")
    private Integer seq;

    @Column(name="mstatus")
    private Integer mstatus;

    @Column(name="remark")
    private String remark;

    @OneToMany(mappedBy = "parentModule", fetch = FetchType.EAGER)
    @OrderBy("seq asc")
    @JsonManagedReference
    List<Module> children;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    @JsonBackReference
	Module parentModule;

    @ManyToMany(mappedBy = "modules", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Role> roles;
}