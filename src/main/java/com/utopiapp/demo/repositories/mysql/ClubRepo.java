package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.Coordinator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ClubRepo extends JpaRepository<Club, Long> {
    boolean existsByAccessCode(String accessCode);
    Page<Club> findAll(Pageable pageable);
    Club findClubById(Long id);
    Page<Club> findAllByNameContainingOrderByName(String name, Pageable paging);
    Club findClubByVolunteersIn(Set<Client> clients);
    Club findClubByAccessCode(String code);
    Club findClubByCoordinatorsIn(Set<Coordinator> coordinators);
    Club findClubByName(String name);
    Club findClubByNameAndIdIsNot(String name, Long id);
    Club findClubByEmail(String name);
    Club findClubByCif(String cif);

}
