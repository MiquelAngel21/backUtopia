package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepo extends JpaRepository<Heart, Long> {
    List<Heart> findHeartsByClient(Client client);
    List<Heart> findHeartsByActivity_Id(Long id);
    Boolean existsByClientAndActivity(Client client, Activity activity);
}
