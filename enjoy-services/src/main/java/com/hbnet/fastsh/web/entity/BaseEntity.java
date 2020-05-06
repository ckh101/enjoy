package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.Update;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Setter
@Getter
public abstract class BaseEntity implements Serializable {


    @Id
    @NotNull(groups = Update.class)
    @TableGenerator(name = "smartad_keygen",
            table = "smartad_keygen",
            pkColumnName = "table_name",
            valueColumnName = "last_id",
            allocationSize = 10,
            initialValue = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "smartad_keygen")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="update_time")
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
