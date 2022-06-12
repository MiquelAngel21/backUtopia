package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ClubRepo extends JpaRepository<Club, Long> {
    boolean existsByAccessCode(String accessCode);
    Page<Club> findAll(Pageable pageable);
    Club findClubByClientsIn(Set<Client> clients);
}
