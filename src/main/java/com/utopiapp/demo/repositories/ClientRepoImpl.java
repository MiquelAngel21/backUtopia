package com.utopiapp.demo.repositories;

import com.utopiapp.demo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepoImpl extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
}
