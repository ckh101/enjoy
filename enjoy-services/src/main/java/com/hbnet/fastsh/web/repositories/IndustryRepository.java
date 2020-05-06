package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.Industry;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IndustryRepository extends BaseRepository<Industry, Long> {

    @Query(value = "select * from smartad_industry a where parentId = 0", nativeQuery = true)
    List<Industry> getAllParentList();

    @Query(value = "select * from smartad_industry a where parentId > 0", nativeQuery = true)
    List<Industry> getAllChildrenList();

    Boolean existsByIndustry(String industry);
}
