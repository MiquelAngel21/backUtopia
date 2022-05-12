package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.UserMain;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    List<Map<String, Object>> getAllActivitiesByMostRecentDate();
    List<Map<String, Object>> getActivitiesByUserAndMostRecentDate(Long clientId);

    List<Map<String, Object>> getTopThreeActivitiesByRangeOfDates();

    Activity createNewActivity(Activity activity, ActivityDto activityDto, UserMain userMain);

    Activity updateAnExistingActivity(Long id, Activity activity, ActivityDto activityDto, UserMain userMain);

    void deleteActivity(Long id);
}
