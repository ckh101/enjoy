package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.Advertiser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdvertiserRepository extends  BaseRepository<Advertiser, Long>  {

    Long countByWxAppIdNotNullAndWxAuthStatusEquals(Integer wxAuthStatus);

    Long countByAuthStatusEquals(Integer authStatus);

    Boolean existsByAccountIdEquals(Integer accountId);

    Advertiser findFirstByAccountId(Integer accountId);

    List<Advertiser> findByIdIn(Long[] ids);

    List<Advertiser> findAllByAuthStatusAndSystemStatus(Integer authStatus, String systemStatus);

    @Query(value = "select a from smartad_advertiser  a inner join smartad_advertiser_user au on a.id = au.advertiser_id where au.user_id = :userId and au.advertiser_id = :id limit 1 ", nativeQuery = true)
    Advertiser findByUserIdAndId(Long userId, Long id);

    @EntityGraph(value = "user.all", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Page<Advertiser> findAll(Pageable pageable);
}
