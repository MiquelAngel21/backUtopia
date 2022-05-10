package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepoMysqlImpl extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
}
