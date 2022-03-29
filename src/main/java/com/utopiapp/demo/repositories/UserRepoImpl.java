package com.utopiapp.demo.repositories;

import com.utopiapp.demo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepoImpl extends JpaRepository<Client, Long> {

}
