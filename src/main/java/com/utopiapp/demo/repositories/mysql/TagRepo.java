package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {
    List<Tag> findAll();
    List<Tag> findByActivitiesIn(Set<Activity> activities);
}
