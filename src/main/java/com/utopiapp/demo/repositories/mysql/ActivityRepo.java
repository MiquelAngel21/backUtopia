package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepo extends JpaRepository<Activity, Long> {

    Page<Activity> findAllByOrderByCreatedDateDescIdDesc(Pageable pageable);
    Page<Activity> findAllByNameLikeOrderByCreatedDateDescIdDesc(String name, Pageable pageable);
    Page<Activity> findAllByHearts_ClientOrderByCreatedDateDesc (Client client, Pageable pageable);
    Page<Activity> findAllByHearts_ClientAndNameLikeOrderByCreatedDateDesc(Client client, String name, Pageable pageable);
    Page<Activity> findAllByClientOrderByCreatedDateDesc(Client client, Pageable pageable);
    Page<Activity> findAllByClientAndNameLikeOrderByCreatedDateDesc(Client client, String name, Pageable pageable);
    List<Activity> findAllByClientOrderByCreatedDateDesc(Client client);

    @Query("SELECT a FROM Heart h, Activity a WHERE h.activity.id = a.id GROUP BY h.activity.id ORDER BY COUNT(h.activity.id) DESC, a.createdDate DESC")
    Page<Activity> findAllByMoreLikedActivities(Pageable pageable);

    @Query("SELECT a FROM Heart h, Activity a WHERE h.activity.id = a.id AND a.name LIKE :searcher GROUP BY h.activity.id ORDER BY COUNT(h.activity.id) DESC, a.createdDate DESC")
    Page<Activity> findAllByMoreLikedActivitiesAndNameLike(@Param("searcher") String searcher, Pageable pageable);



    Activity findActivityById(Long id);
    Activity findActivityByName(String name);
    Activity findActivityByClientAndId(Client currentUser, Long id);
    Boolean existsByNameAndIdIsNot(String name, Long id);

}
