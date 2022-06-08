package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
    Client findByActivitiesInAndId(Set<Activity> activities, Long Id);
}
