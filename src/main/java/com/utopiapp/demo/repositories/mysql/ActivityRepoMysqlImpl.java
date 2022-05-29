package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepoMysqlImpl extends JpaRepository<Activity, Long> {
    Page<Activity> findAllByOrderByCreatedDateDescIdDesc(Pageable pageable);
    List<Activity> findAllByClientOrderByCreatedDateDesc(Long clientId);

    Page<Activity> findAllByNameLikeOrderByCreatedDateDescIdDesc(String name, Pageable pageable);

    @Query("SELECT a FROM Heart h, Activity a WHERE h.activity.id = a.id AND a.createdDate > :startRange AND a.createdDate < :endRange GROUP BY h.activity.id ORDER BY COUNT(h.activity.id) DESC, a.createdDate DESC")
    List<Activity> getTopThreeFromRangeOfDates(@Param("startRange") LocalDateTime startRange, @Param("endRange") LocalDateTime endRange);
    List<Activity> findAllByOrderByCreatedDateAsc();
    Activity getActivityByName(String name);
    Activity findActivityByClientAndId(Client currentUser, Long id);
}
