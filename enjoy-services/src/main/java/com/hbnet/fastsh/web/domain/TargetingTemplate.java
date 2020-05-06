package com.hbnet.fastsh.web.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetingTemplate {
    private Long id;
    private String templateName;

    public TargetingTemplate(Long id, String templateName){
        this.id = id;
        this.templateName = templateName;
    }
}
