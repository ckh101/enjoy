package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.WxUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface WxUserRepository extends BaseRepository<WxUser, Long> {

    WxUser findByOpenId(String openId);

    WxUser findByPhone(String phone);

    @EntityGraph(value = "user.all", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Page<WxUser> findAll(Pageable pageable);

}
