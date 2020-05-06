package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.WxApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface WxAppRepository extends BaseRepository<WxApp, Long> {

    Boolean existsByAppName(String appName);

    WxApp findByAppId(String appId);

    List<WxApp> findByIdIn(Long[] ids);

    @EntityGraph(value = "user.all", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Page<WxApp> findAll(Pageable pageable);
}

