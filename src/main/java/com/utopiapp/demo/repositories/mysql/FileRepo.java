package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepo extends JpaRepository<File, Long> {
    boolean existsByContent(byte[] content);
    File findByContent(byte[] content);
    Optional<File> findById(Long id);
}
