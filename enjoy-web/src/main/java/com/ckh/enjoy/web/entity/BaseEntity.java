package com.ckh.enjoy.web.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -3816215563098432109L;


    private String id;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String getId(){
        return  this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updateTime;

    @PrePersist
    void createdAt() {
        this.createTime = this.updateTime = new Date();
    }

    @PreUpdate
    void updatedAt() {
        this.updateTime = new Date();
    }
}
