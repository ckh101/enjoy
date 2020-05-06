package com.hbnet.fastsh.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name="smartad_role_module")
@Setter
@Getter
public class RoleModule implements Serializable{
	private static final long serialVersionUID = -5154244998568636941L;
	@Id
    @Column(name="role_id")
	private Long roleId;

	@Id
    @Column(name="module_id")
	private Long moduleId;

}
