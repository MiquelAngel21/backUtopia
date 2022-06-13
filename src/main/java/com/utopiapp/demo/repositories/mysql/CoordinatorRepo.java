package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatorRepo extends JpaRepository<Coordinator, Long> {

}
