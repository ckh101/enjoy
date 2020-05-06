package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface UserRepository extends BaseRepository<User, Long> {

    boolean existsByAccount(String account);

    boolean existsByPhone(String phone);

    User findFirstByPhone(String phone);

    User findByAccount(String account);

    @EntityGraph(value = "user.all", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Page<User> findAll(Pageable pageable);
}
