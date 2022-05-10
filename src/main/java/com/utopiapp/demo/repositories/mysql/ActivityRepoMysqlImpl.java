package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepoMysqlImpl extends JpaRepository<Activity, Long> {
    List<Activity> findAllByOrderByCreatedDateAsc();
}
