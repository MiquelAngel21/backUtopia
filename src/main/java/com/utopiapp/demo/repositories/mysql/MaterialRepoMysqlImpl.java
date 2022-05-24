package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.File;
import com.utopiapp.demo.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepoMysqlImpl extends JpaRepository<Material, Long> {
    Material findByNameAndAmount(String name, int amount);
}
