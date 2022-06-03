package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepoMysqlImpl extends JpaRepository<Material, Long> {
    Material findByNameAndAmount(String name, int amount);
    List<Material> getMaterialsByActivities_Id(Long id);
}
