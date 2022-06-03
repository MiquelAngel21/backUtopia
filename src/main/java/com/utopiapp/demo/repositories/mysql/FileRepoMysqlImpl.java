package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepoMysqlImpl extends JpaRepository<File, Long> {
    File findByContent(byte[] content);
}
