package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepo extends JpaRepository<Heart, Long> {
}
