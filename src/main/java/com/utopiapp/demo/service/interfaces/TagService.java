package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface TagService {
    List<Tag> getAll() throws Exception;
    List<Tag> getTagsOfActivity(Set<Activity> activities);
}
