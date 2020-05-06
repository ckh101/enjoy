package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.TargetingTemplate;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TargetingTemplateRepository extends BaseRepository<TargetingTemplate, Long> {
    @Query("select new com.hbnet.fastsh.web.domain.TargetingTemplate(id, templateName) from TargetingTemplate t")
    List<com.hbnet.fastsh.web.domain.TargetingTemplate> findList();
}
