package com.hbnet.fastsh.web.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="smartad_role")
@Setter
@Getter
public class Role  extends BaseEntity{
	private static final long serialVersionUID = -8639773932212484097L;

	@Column(name="role_name")
	private String roleName;

	@Column(name="flag_str")
	private String flagStr;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="smartad_role_module",joinColumns = @JoinColumn(name="role_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name="module_id",referencedColumnName = "id"))
    @JsonIgnore
	private List<Module> modules;
}
