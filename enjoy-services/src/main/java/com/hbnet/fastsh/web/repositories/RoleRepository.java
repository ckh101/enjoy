package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface RoleRepository extends BaseRepository<Role, Long> {

    Role findByRoleName(String roleName);

    @EntityGraph(value = "user.all", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Page<Role> findAll(Pageable pageable);
}
