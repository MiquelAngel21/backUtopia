package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Petition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetitionRepo extends JpaRepository<Petition, Long> {

}
