package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.GdtLocation;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GdtLocationRepository extends BaseRepository<GdtLocation, Long> {

    @Query(value = "select a from smartad_gdt_location where parentId = :parentId", nativeQuery = true)
    List<GdtLocation> findByParentId(Long parentId);

    List<GdtLocation> findByParentNameIsNot(String parentName);
}
