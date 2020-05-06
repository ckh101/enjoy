package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "smartad_user_role")
@Getter
@Setter
public class UserRole implements Serializable {

    private static final long serialVersionUID = 2989819695370796392L;

    @Id
    @Column(name="user_id")
    private Long userId;

    @Id
    @Column(name="role_id")
    private Long roleId;
}
