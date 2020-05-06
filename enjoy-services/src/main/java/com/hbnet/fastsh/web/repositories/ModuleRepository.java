package com.hbnet.fastsh.web.repositories;

import com.hbnet.fastsh.web.entity.Module;

import java.util.List;

public interface ModuleRepository extends BaseRepository<Module, Long> {

    Module findFirstByMname(String mname);

    List<Module> findAllByLevelLessThan(Integer level);

}

