package com.hbnet.fastsh.web.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "smartad_gdt_location")
@Getter
@Setter
public class GdtLocation extends BaseEntity {


    private String cityName;

    private String parentName;

    private String cityLevel;

    private String areaType;


    @OneToMany(mappedBy = "parentGdtLocation", fetch = FetchType.EAGER)
    @JsonManagedReference
    List<GdtLocation> children;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonBackReference
    GdtLocation parentGdtLocation;

    public void jsonToObj(JSONObject json){
        this.setCityLevel(json.getString("city_level"));
        this.setCityName(json.getString("name"));
        this.parentGdtLocation.setId(json.getLong("parent_id"));
        this.setParentName(json.getString("parent_name")==null?"":json.getString("parent_name"));
    }
}
