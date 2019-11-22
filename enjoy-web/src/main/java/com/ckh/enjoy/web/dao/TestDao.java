package com.ckh.enjoy.web.dao;

import com.ckh.enjoy.web.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDao extends JpaRepository<Test, String> {
}
