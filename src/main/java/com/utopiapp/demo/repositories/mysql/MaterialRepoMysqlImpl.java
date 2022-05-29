package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.File;
import com.utopiapp.demo.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface MaterialRepoMysqlImpl extends JpaRepository<Material, Long> {
    Material findByNameAndAmount(String name, int amount);
    List<Material> getMaterialsByActivity_Id(Long id);
}
