package com.hbnet.fastsh.web.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "smartad_industry")
@Getter
@Setter

public class Industry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @NotFound(action= NotFoundAction.IGNORE)
    private Industry parent;
    private String industry;


}
