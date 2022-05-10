package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.UserMain;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    List<Map<String, Object>> getAllActivitiesByMostRecentDate();
    Activity createNewActivity(ActivityDto activityDto, UserMain userMain);
}
