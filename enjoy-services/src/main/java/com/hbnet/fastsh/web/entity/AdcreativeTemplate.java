package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "smartad_adcreative_templates")
@Getter
@Setter
public class AdcreativeTemplate extends BaseEntity {


    private Integer adCreativeTemplateId;

    private String adCreativeTemplateName;

    private String adCreativeTemplateDescription;

    private String adCreativeTemplateSize;

    private String adCreativeTemplateStyle;

    private String siteSet;

    private String promotedObjectType;

    private String adcreativeSampleImageList;

    private String adAttributes;

    private String adCreativeAttributes;

    private String adCreativeElements;

    private String dynamicProductAdEnabled;

    private String productItemsQuantityMode;

    private String supportPageType;

    private String supportBillingSpecList;


}
