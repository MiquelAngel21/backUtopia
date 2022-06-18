package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepo extends JpaRepository<File, Long> {
    Optional<File> findById(Long id);
    File findFileByClient_id(Long clientId);
    List<File> findAllByContent(byte[] content);
}
