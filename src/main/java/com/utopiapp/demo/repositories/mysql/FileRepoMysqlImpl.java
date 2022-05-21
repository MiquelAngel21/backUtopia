package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepoMysqlImpl extends JpaRepository<File, Long> {
}
