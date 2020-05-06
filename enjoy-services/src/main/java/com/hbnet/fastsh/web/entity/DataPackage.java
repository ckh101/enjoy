package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "smartad_data_package")
@Getter
@Setter
public class DataPackage extends BaseEntity {

    private String packageName;

    private String packageUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="smartad_datapackage_industry",joinColumns = @JoinColumn(name="packageId",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="industryId",referencedColumnName = "id"))
    List<Industry> industries;
}
