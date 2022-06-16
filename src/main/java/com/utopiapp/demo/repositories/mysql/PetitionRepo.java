package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.Petition;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PetitionRepo extends JpaRepository<Petition, Long> {
    Page<Petition> findAllByClubOrderByCreatedDateDesc(Club club, Pageable pageable);
    Petition findPetitionById(Long id);
    List<Petition> findAllByClientAndClubOrderByCreatedDateDesc(Client client, Club club);
}
