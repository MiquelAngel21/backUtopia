package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepoMysqlImpl extends JpaRepository<Material, Long> {
    List<Material> getMaterialsByActivity_Id(Long id);
}
