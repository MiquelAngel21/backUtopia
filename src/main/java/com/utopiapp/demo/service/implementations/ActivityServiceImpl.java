package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.repositories.mysql.ActivityRepoMysqlImpl;
import com.utopiapp.demo.service.interfaces.ActivityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepoMysqlImpl activityRepoMysql;

    public ActivityServiceImpl(ActivityRepoMysqlImpl activityRepoMysql) {
        this.activityRepoMysql = activityRepoMysql;
    }

    @Override
    public List<Map<String, Object>> getAllActivitiesByMostRecentDate() {
        List<Activity> activities = activityRepoMysql.findAllByOrderByCreatedDateAsc();
        List<Map<String, Object>> allActivitiesJson = new ArrayList<>();
        for (Activity activity : activities){
            allActivitiesJson.add(createActivityJson(activity));
        }
        return allActivitiesJson;
    }

    private Map<String, Object> createActivityJson(Activity activity) {
        Map<String, Object> activityJson = new HashMap<>();
        activityJson.put("id", activity.getId());
        activityJson.put("name", activity.getName());
        activityJson.put("isEvent", activity.isEvent());
        activityJson.put("description", activity.getDescription());
        activityJson.put("createdDate", activity.getCreatedDate());
        activityJson.put("guides", activity.getGuide());
        activityJson.put("client", activity.getClient().getId());
        activityJson.put("tags", activity.getTags());
        activityJson.put("materials", activity.getMaterials());
        activityJson.put("files", activity.getFiles());
        activityJson.put("hearts", activity.getHearts());
        return activityJson;
    }

    @Override
    public Activity createNewActivity(ActivityDto activityDto, UserMain userMain) {
        Activity activity = new Activity(activityDto.getName(), activityDto.isEvent(), activityDto.getDescription(), LocalDateTime.now(), userMain.toClient(), activityDto.getTags(), activityDto.getMaterials(), activityDto.getFiles());
        return activityRepoMysql.save(activity);
    }


}
