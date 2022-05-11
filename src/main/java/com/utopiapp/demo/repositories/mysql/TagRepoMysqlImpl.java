package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepoMysqlImpl extends JpaRepository<Tag, Long> {
    List<Tag> findAll();
}
