package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
    List<Client> findAllByClubOrderByCreatedDateDesc(Club club);
    Page<Client> findAllByClubOrderByCreatedDateDesc(Club club, Pageable pageable);
    Client findClientById(Long id);
    Page<Client> findAllByNameLikeAndClubOrderByCreatedDateDesc(String filter, Club club, Pageable pageable);
    boolean existsClientByEmail(String email);
    boolean existsClientByUsername(String username);
    Client findClientByUsername(String username);
}
