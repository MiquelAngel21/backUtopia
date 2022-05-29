package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.model.*;
import com.utopiapp.demo.repositories.mysql.ActivityRepoMysqlImpl;
import com.utopiapp.demo.service.interfaces.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    MaterialServiceImpl materialService;

    @Autowired
    ActivityRepoMysqlImpl activityRepoMysql;

    @Autowired
    FileServiceImpl fileService;

    @Autowired
    TagServiceImpl tagService;

    @Override
    public List<Map<String, Object>> getAllActivitiesByMostRecentDate() {
        List<Activity> activities = activityRepoMysql.findAllByOrderByCreatedDateDesc();
        return convertActivityListIntoJsonList(activities);
    }

    @Override
    public List<Map<String, Object>> getActivitiesByUserAndMostRecentDate(Long clientId) {
        List<Activity> activities = activityRepoMysql.findAllByClientOrderByCreatedDateDesc(clientId);
        return convertActivityListIntoJsonList(activities);
    }

    @Override
    public List<Map<String, Object>> getTopThreeActivitiesByRangeOfDates() {
        //MILLORS TRES DE LA SETMANA
        LocalDateTime startDateRange = getFirstDayOfTheWeek();
        LocalDateTime endDateRange = getLastDayOfTheWeek(startDateRange);
        //MILLORS TRES DEL MES
        LocalDateTime startMonthDayRange = getFirstDayOfTheMonth();
        LocalDateTime endMonthDayRange = getLastDayOfTheMonth(startMonthDayRange);
        //MILLORS TRES DE LA HISTORIA
        LocalDateTime startofAllHistory = LocalDateTime.parse("0001-01-01T00:00:00.000");
        LocalDateTime endofAllHistory = LocalDateTime.now();

        List<Activity> activities = activityRepoMysql.getTopThreeFromRangeOfDates(startofAllHistory, endofAllHistory).subList(0,3);
        return convertActivityListIntoJsonList(activities);
    }

    private LocalDateTime getLastDayOfTheMonth(LocalDateTime startMonthDayRange) {
        int endMonthDay = getDaysOfMonthByNumericRepresentationAndYear(startMonthDayRange.getMonthValue()-1, startMonthDayRange.getYear());
        return startMonthDayRange.withDayOfMonth(endMonthDay);
    }

    private LocalDateTime getFirstDayOfTheMonth() {
        LocalDateTime startMonthDayRange = LocalDateTime.parse(LocalDate.now()+"T00:00:00.000");
        return startMonthDayRange.withDayOfMonth(1);
    }

    private LocalDateTime getLastDayOfTheWeek(LocalDateTime startDateRange) {
        LocalDateTime endDateRange =  startDateRange.plusDays(8);
        endDateRange = endDateRange.withHour(0);
        endDateRange = endDateRange.withMinute(0);
        endDateRange = endDateRange.withSecond(0);
        endDateRange = endDateRange.withNano(0);
        return endDateRange;
    }

    private LocalDateTime getFirstDayOfTheWeek() {
        LocalDateTime now = LocalDateTime.parse(LocalDate.now()+"T23:59:59.9999");
        TemporalField fieldISO = WeekFields.of(Locale.FRENCH).dayOfWeek();
        LocalDateTime startDateRange = now.with(fieldISO, 1);
        return startDateRange;
    }

    @Override
    public Activity createNewActivity(Activity activity, ActivityDto activityDto, UserMain userMain) {
        activity.setName(activityDto.getName());
        activity.setEvent(activityDto.isEvent());
        activity.setDescription(activityDto.getDescription());
        activity.setCreatedDate(LocalDateTime.now());
        activity.setClient(userMain.toClient());
        activity.setTags(activityDto.getTags());
        activity.setMaterials(activityDto.getMaterials());
        activity.setFiles(activityDto.getFiles());
        Activity activityCreated = activityRepoMysql.save(activity);
        materialService.saveMaterial(activityDto.getMaterials(), activityCreated);
        fileService.saveFiles(activityDto.getFiles(), activityCreated);
        return activityCreated;
    }

    @Override
    public Activity updateAnExistingActivity(Long id, Activity activity, ActivityDto activityDto, UserMain userMain) {
        activity.setId(id);
        return createNewActivity(activity, activityDto, userMain);
    }

    @Override
    public void deleteActivity(Long id) {
        activityRepoMysql.delete(activityRepoMysql.getById(id));
    }

    private List<Map<String, Object>> convertActivityListIntoJsonList(List<Activity> activities) {
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
        activityJson.put("hearts", heartsToJson(activity.getHearts()));
        return activityJson;
    }


    @Override
    public Activity getActivityByName(String name) {
        Activity activity = activityRepoMysql.getActivityByName(name);
        return activity;
    }

    @Override
    public Activity getActivityById(Long id) {
        Activity activity = activityRepoMysql.findById(id).orElseThrow();
        return activity;
    }

    @Override
    public Map<String, Object> getActivityDataJson(Long id) {
        Map<String, Object> jsonData = new HashMap<>();
        Activity activity = getActivityById(id);
        Set<Activity> activities = new HashSet<>();
        activities.add(activity);
        List<Material> materials = materialService.getMaterialByActivity(id);
        List<Tag> tags = tagService.getTagsOfActivity(activities);
        jsonData.put("activity", activity);
        jsonData.put("materials", materials);
        jsonData.put("activity_tags", tags);
        return jsonData;
    }

    @Override
    public boolean isOwner(Client currentClient, Long activityId) {
        Activity activity = activityRepoMysql.findByClientAndId(currentClient, activityId);
        if (activity != null) {
            return true;
        }
        return false;
    }

    private List<Map<String, Object>> heartsToJson(Set<Heart> hearts) {
        Map<String, Object> heartJson = new HashMap<>();
        List<Map<String, Object>> allHeartsByActivity = new ArrayList<>();
        for (Heart heart : hearts){
            heartJson.put("id", heart.getId());
            heartJson.put("activityId", heart.getActivity().getId());
            heartJson.put("clientId", heart.getClient().getId());
            allHeartsByActivity.add(heartJson);
        }
        return allHeartsByActivity;
    }

    private static int getDaysOfMonthByNumericRepresentationAndYear(int mes, int año){
        switch(mes){
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            case 1:
                if ( ((año%100 == 0) && (año%400 == 0)) ||
                        ((año%100 != 0) && (año%  4 == 0))   )
                    return 29;
                else
                    return 28;
            default:
                throw new java.lang.IllegalArgumentException(
                        "El mes debe estar entre 0 y 11");
        }
    }


}
