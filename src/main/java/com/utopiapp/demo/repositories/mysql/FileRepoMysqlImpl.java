package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.File;
import com.utopiapp.demo.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepoMysqlImpl extends JpaRepository<File, Long> {

}
