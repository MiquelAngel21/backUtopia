package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoordinatorRepo extends JpaRepository<Coordinator, Long> {
    List<Coordinator> findAllByClub(Club club);
}
