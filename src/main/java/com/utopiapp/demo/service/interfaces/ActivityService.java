package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Tag;
import com.utopiapp.demo.model.UserMain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Page<Activity> getAllActivitiesByMostRecentDate(Pageable paging);
    List<Map<String, Object>> getActivitiesByUserAndMostRecentDate(Long clientId);

    Activity createNewActivity(Activity activity, ActivityDto activityDto, UserMain userMain, boolean isUpdate);

    void updateAnExistingActivity(Long id, Activity activity, ActivityDto activityDto, UserMain userMain);

    void deleteActivity(Long id);

    Map<String, Object> getOneActivityById(Long id);

    List<Map<String, Object>> convertActivityListIntoJsonList(List<Activity> content);

    Map<String, Object> manageLike(Long id, UserMain userMain);

    Map<String, Object> makePaginationWithDatabaseResults(Client client, String typeOfSearch, String filterText, int start, int length);

    Activity getActivityByName(String name);
    Activity getActivityById(Long id);

    Map<String, Object> getActivityDataJson(Long id);
    boolean isOwner(Client currentClient, Long activityId);

    List<Tag> getAllTags();

    Map<String, Object> clientWithNewActivity(Client client, Map<String, Object> activityWithNewLike);
}
