package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "smartad_datapackage_industry")
@Getter
@Setter
public class DataPackageIndustry implements Serializable {

    @Id
    private long packageId;
    @Id
    private long industryId;


}
