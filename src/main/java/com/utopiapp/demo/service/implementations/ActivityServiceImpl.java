package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Heart;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.repositories.mysql.ActivityRepoMysqlImpl;
import com.utopiapp.demo.service.interfaces.ActivityService;
import com.utopiapp.demo.service.interfaces.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepoMysqlImpl activityRepoMysql;
    private final FileRepoMysqlImpl fileRepoMysql;
    private final MaterialRepoMysqlImpl materialRepoMysql;
    private final HeartRepoMysqlImpl heartRepoMysql;

    public ActivityServiceImpl(ActivityRepoMysqlImpl activityRepoMysql, FileRepoMysqlImpl fileRepoMysql, MaterialRepoMysqlImpl materialRepoMysql, HeartRepoMysqlImpl heartRepoMysql) {
        this.activityRepoMysql = activityRepoMysql;
        this.fileRepoMysql = fileRepoMysql;
        this.materialRepoMysql = materialRepoMysql;
        this.heartRepoMysql = heartRepoMysql;
    }

    @Override
    public Page<Activity> getAllActivitiesByMostRecentDate(Pageable paging) {
        return activityRepoMysql.findAllByOrderByCreatedDateDescIdDesc(paging);
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
        activity.setHearts(new HashSet<>());

        addMaterialsToActivity(activity, activityDto);
        addFilesToActivity(activity, activityDto);

        activity = activityRepoMysql.save(activity);


        return activity;
    }

    private void addMaterialsToActivity(Activity activity, ActivityDto activityDto) {
        Set<Material> finalMaterial = new HashSet<>();
        for (Material material : activityDto.getMaterials()) {
            Material materialAlreadyExists = materialRepoMysql.findByNameAndAmount(material.getName(), material.getAmount());

            if (materialAlreadyExists != null) {
                finalMaterial.add(materialAlreadyExists);
            } else {
                noRareCharactersInText(material.getName());
                if (material.getAmount() > 0) {
                    finalMaterial.add(material);
                    materialRepoMysql.save(material);
                } else {
                    throw new RuntimeException("La quantitat ha de ser superior a 0");
                }

            }
        }
        activity.setMaterials(finalMaterial);
    }

    public void noRareCharactersInText(String text) {
        String newText = text.replaceAll("['*\\-\"\\\\/\\[\\]?¿!¡<>=]*", "");
        if (!newText.equals(text)) {
            throw new RuntimeException("No rare characters");
        }
    }

    private void addFilesToActivity(Activity activity, ActivityDto activityDto) {
        Set<File> finalFiles = new HashSet<>();
        for (FileDto fileDto : activityDto.getFiles()) {
            File file = fileDtoIntoFile(fileDto);
            File fileAlreadyExists = fileRepoMysql.findByContent(file.getContent());

            if (fileAlreadyExists != null) {
                finalFiles.add(fileAlreadyExists);
            } else {
                finalFiles.add(file);
                fileRepoMysql.save(file);
            }
        }
        activity.setFiles(finalFiles);
    }

    private File fileDtoIntoFile(FileDto fileDto) {
        File file = new File();
        file.setContent(Base64.getDecoder().decode(fileDto.getContent()));
        file.setName(fileDto.getName());
        file.setMediaType(fileDto.getMediaType());
        return file;
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

    @Override
    public Map<String, Object> getOneActivityById(Long id) {
        Activity activity = activityRepoMysql.getById(id);
        return createActivityJson(activity);
    }

    @Override
    public List<Map<String, Object>> convertActivityListIntoJsonList(List<Activity> activities) {
        List<Map<String, Object>> allActivitiesJson = new ArrayList<>();
        for (Activity activity : activities) {
            allActivitiesJson.add(createActivityJson(activity));
        }
        return allActivitiesJson;
    }

    @Override
    public Map<String, Object> manageLike(Long id, UserMain userMain) {
        Activity activity = activityRepoMysql.getById(id);
        boolean isLiked = false;
        for (Heart heart : activity.getHearts()) {
            if (heart.getClient().equals(userMain.toClient())) {
                Set<Heart> allHeartsFromActivity = activity.getHearts();
                allHeartsFromActivity.remove(heart);
                activityRepoMysql.save(activity);
                heartRepoMysql.delete(heart);
                isLiked = true;
                break;
            }
        }
        if (!isLiked) {
            Heart newLike = new Heart(activity, userMain.toClient());
            Set<Heart> addNewLike = activity.getHearts();
            addNewLike.add(newLike);
            activity.setHearts(addNewLike);
            heartRepoMysql.save(newLike);
        }
        return createActivityJson(activity);
    }

    @Override
    public Map<String, Object> makePaginationWithDatabaseResults(String filterText, int start, int length) {
        Pageable paging = PageRequest.of(start / length, length);
        Page<Activity> pageResult;
        if (filterText != null && filterText.length() != 0){
            pageResult = getAllFilteredActivitiesByMostRecentDate("%"+filterText+"%", paging);
        } else {
            pageResult = getAllActivitiesByMostRecentDate(paging);
        }

        long total = pageResult.getTotalElements();
        List<Map<String, Object>> data = convertActivityListIntoJsonList(pageResult.getContent());

        Map<String, Object> json = new HashMap<>();
        json.put("data", data);
        json.put("recordsTotal", total);
        return json;

    }

    private Page<Activity> getAllFilteredActivitiesByMostRecentDate(String filteredText, Pageable paging) {
        return activityRepoMysql.findAllByNameLikeOrderByCreatedDateDescIdDesc(filteredText, paging);
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
        for (Heart heart : hearts) {
            heartJson.put("id", heart.getId());
            heartJson.put("activityId", heart.getActivity().getId());
            heartJson.put("clientId", heart.getClient().getId());
            allHeartsByActivity.add(heartJson);
        }
        return allHeartsByActivity;
    }

    private static int getDaysOfMonthByNumericRepresentationAndYear(int mes, int año) {
        switch (mes) {
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
                if (((año % 100 == 0) && (año % 400 == 0)) ||
                        ((año % 100 != 0) && (año % 4 == 0)))
                    return 29;
                else
                    return 28;
            default:
                throw new java.lang.IllegalArgumentException(
                        "El mes debe estar entre 0 y 11");
        }
    }


}
