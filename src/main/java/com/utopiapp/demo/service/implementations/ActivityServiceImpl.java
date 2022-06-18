package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.exceptions.*;
import com.utopiapp.demo.jwt.JwtProvider;
import com.utopiapp.demo.model.*;
import com.utopiapp.demo.repositories.mysql.*;
import com.utopiapp.demo.service.interfaces.ActivityService;
import com.utopiapp.demo.service.interfaces.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepo activityRepoMysql;
    private final FileRepo fileRepoMysql;
    private final MaterialRepo materialRepoMysql;
    private final HeartRepo heartRepoMysql;
    private final TagRepo tagRepoMysql;
    private final ClientService clientService;
    private final JwtProvider jwtProvider;

    public ActivityServiceImpl(ActivityRepo activityRepoMysql, FileRepo fileRepoMysql, MaterialRepo materialRepoMysql, HeartRepo heartRepoMysql, TagRepo tagRepoMysql, ClientServiceImpl clientService, JwtProvider jwtProvider) {
        this.activityRepoMysql = activityRepoMysql;
        this.fileRepoMysql = fileRepoMysql;
        this.materialRepoMysql = materialRepoMysql;
        this.heartRepoMysql = heartRepoMysql;
        this.tagRepoMysql = tagRepoMysql;
        this.clientService = clientService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Page<Activity> getAllActivitiesByMostRecentDate(Pageable paging) {
        return activityRepoMysql.findAllByOrderByCreatedDateDescIdDesc(paging);
    }

    @Override
    public List<Map<String, Object>> getActivitiesByUserAndMostRecentDate(Client client) {
        List<Activity> activities = activityRepoMysql.findAllByClientOrderByCreatedDateDesc(clientService.getClientById(client.getId()));
        return convertActivityListIntoJsonList(activities, client);
    }

    @Override
    public Activity createNewActivity(Activity activity, ActivityDto activityDto, UserMain userMain, boolean isUpdate) {
        validateFieldsOfActivity(activityDto);
        activity.setName(activityDto.getName());
        activity.setDescription(activityDto.getDescription());
        activity.setCreatedDate(LocalDateTime.now());
        activity.setClient(userMain.toClient());
        activity.setTags(activityDto.getTags());
        addMaterialsToActivity(activity, activityDto);
        activity = activityRepoMysql.save(activity);
        return activity;
    }

    private void validateFieldsOfActivity(ActivityDto activityDto) {
        noRareCharactersInText(activityDto.getName());
        noRareCharactersInText(activityDto.getDescription());
        for (Tag tag : activityDto.getTags()){
            if (!tagRepoMysql.existsByName(tag.getName())){
                throw new TagNoExists();
            }
            noRareCharactersInText(tag.getName());
        }
        for (Material material : activityDto.getMaterials()){
            noRareCharactersInText(material.getName());
            material.setName(material.getName().trim());
            if (material.getAmount() < 0){
                throw new NegativeAmountException();
            }
        }
        if (activityDto.getName().isEmpty() || activityDto.getDescription().isEmpty() || activityDto.getTags().isEmpty()){
            throw new EmptyFieldsException();
        }
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
                }
            }
        }
        activity.setMaterials(finalMaterial);
    }

    public void noRareCharactersInText(String text) {
        String newText = text.replaceAll("['*\\-\"\\\\/\\[\\]?¿!¡<>=%()&|#$¬~·ºª]*", "");
        if (!newText.equals(text)) {
            throw new RareCharacterException();
        }
    }

    @Override
    public void updateAnExistingActivity(Long id, Activity activity, ActivityDto activityDto, UserMain userMain) {
        if ((activityDto.getName().isEmpty() || activityDto.getDescription().isEmpty()) || activityDto.getTags().isEmpty()){
            throw new EmptyFieldsException();
        }
        activity.setId(id);
        createNewActivity(activity, activityDto, userMain, true);
    }

    @Override
    public void deleteActivity(Long id, UserMain user) {
        if(!isOwner(user.toClient(), id)){
            throw new UnauthorizedException();
        }
        Activity activityToDelete = activityRepoMysql.getById(id);
        List<Heart> heartsOfActivity = heartRepoMysql.findHeartsByActivity_Id(id);
        heartRepoMysql.deleteAll(heartsOfActivity);
        activityRepoMysql.delete(activityToDelete);
    }

    @Override
    public Map<String, Object> getOneActivityById(Long id, Client currentClient) {
        Activity activity = activityRepoMysql.findActivityById(id);
        return createActivityJson(activity, currentClient);
    }

    @Override
    public List<Map<String, Object>> convertActivityListIntoJsonList(List<Activity> activities, Client currentClient) {
        List<Map<String, Object>> allActivitiesJson = new ArrayList<>();
        for (Activity activity : activities) {
            allActivitiesJson.add(createActivityJson(activity, currentClient));
        }
        return allActivitiesJson;
    }

    @Override
    public Map<String, Object> manageLike(Long id, UserMain userMain) {
        Activity activity = activityRepoMysql.findActivityById(id);
        Client currentClient = userMain.toClient();
        boolean isLiked = false;
        for (Heart heart : activity.getHearts()) {
            if (heart.getClient().equals(currentClient)) {
                unlikeActivity(heart, currentClient, activity);
                isLiked = true;
                break;
            }
        }
        if (!isLiked) {
            likeActivity(currentClient, activity);
        }
        return createActivityJson(activity, currentClient);
    }

    private void likeActivity(Client currentClient, Activity activity) {
        Heart newLike = new Heart(activity, currentClient);
        newLike = heartRepoMysql.save(newLike);
        Set<Heart> newActivitiesLikes = activity.getHearts();
        newActivitiesLikes.add(newLike);
        activity.setHearts(newActivitiesLikes);

        Set<Heart> newClientLikes = currentClient.getHearts();
        newClientLikes.add(newLike);
        currentClient.setHearts(newClientLikes);

        activityRepoMysql.save(activity);
        clientService.save(currentClient);

    }

    private void unlikeActivity(Heart heart, Client currentClient, Activity activity) {
        Set<Heart> allHeartsFromActivity = activity.getHearts();
        allHeartsFromActivity.remove(heart);
        activity.setHearts(allHeartsFromActivity);

        Set<Heart> allHeartsFromClient = currentClient.getHearts();
        for (Heart clientLikes : allHeartsFromClient){
            if (heart.equals(clientLikes)){
                allHeartsFromClient.remove(clientLikes);
                break;
            }
        }
        currentClient.setHearts(allHeartsFromClient);

        clientService.save(currentClient);
        activityRepoMysql.save(activity);
        heartRepoMysql.delete(heart);
    }

    @Override
    public Map<String, Object> makePaginationWithDatabaseResults(Client client, String typeOfSearch, String filterText, int start, int length) {
        Pageable paging = PageRequest.of(start / length, length);
        Page<Activity> pageResult;

        if (typeOfSearch != null && typeOfSearch.length() != 0 && filterText != null && filterText.length() != 0){
            pageResult = getAllFilteredActivitiesByType(client, filterText, typeOfSearch, paging);
        } else if (typeOfSearch != null && typeOfSearch.length() != 0){
            pageResult = getAllActivitiesByType(client, paging, typeOfSearch);
        } else if (filterText != null && filterText.length() != 0 ){
            pageResult = getAllFilteredActivitiesByMostRecentDate("%"+filterText+"%", paging);
        } else {
            pageResult = getAllActivitiesByMostRecentDate(paging);
        }

        long total = pageResult.getTotalElements();
        List<Map<String, Object>> data = convertActivityListIntoJsonList(pageResult.getContent(), client);

        Map<String, Object> json = new HashMap<>();
        json.put("data", data);
        json.put("recordsTotal", total);
        return json;

    }

    private Page<Activity> getAllFilteredActivitiesByType(Client client, String filterText, String typeOfSearch, Pageable paging) {
        filterText = "%"+filterText+"%";
        Page<Activity> activities;
        switch (typeOfSearch){
            case "Ranking":
                activities = activityRepoMysql.findAllByMoreLikedActivitiesAndNameLike(filterText, paging);
                break;
            case "Favorites":
                activities = activityRepoMysql.findAllByHearts_ClientAndNameLikeOrderByCreatedDateDesc(client, filterText, paging);
                break;
            case "MyActivities":
                activities = activityRepoMysql.findAllByClientAndNameLikeOrderByCreatedDateDesc(client, filterText, paging);
                break;
            default:
                activities = null;
        }

        return activities;
    }

    private Page<Activity> getAllActivitiesByType(Client client, Pageable paging, String typeOfSearch) {
        Page<Activity> activities;
        switch (typeOfSearch){
            case "Ranking":
                activities = activityRepoMysql.findAllByMoreLikedActivities(paging);
                break;
            case "Favorites":
                activities = activityRepoMysql.findAllByHearts_ClientOrderByCreatedDateDesc(client,paging);
                break;
            case "MyActivities":
                activities = activityRepoMysql.findAllByClientOrderByCreatedDateDesc(client, paging);
                break;
            default:
                activities = null;
        }

        return activities;
    }

    private Page<Activity> getAllFilteredActivitiesByMostRecentDate(String filteredText, Pageable paging) {
        return activityRepoMysql.findAllByNameLikeOrderByCreatedDateDescIdDesc(filteredText, paging);
    }

    @Override
    public Map<String, Object> createActivityJson(Activity activity, Client currentClient) {
        Map<String, Object> activityJson = new HashMap<>();
        activityJson.put("id", activity.getId());
        activityJson.put("name", activity.getName());
        activityJson.put("description", activity.getDescription());
        activityJson.put("createdDate", activity.getCreatedDate());
        activityJson.put("client", clientService.getClientInJsonFormat(activity.getClient()));
        activityJson.put("tags", activity.getTags());
        activityJson.put("materials", activity.getMaterials());
        activityJson.put("hearts", heartsToJson(activity.getHearts()));
        activityJson.put("liked", isLiked(activity, currentClient));
        return activityJson;
    }

    private Boolean isLiked(Activity activity, Client currentClient) {
        return heartRepoMysql.existsByClientAndActivity(currentClient, activity);
    }

    @Override
    public Activity getActivityByName(String name) {
        Activity activity = activityRepoMysql.findActivityByName(name);
        return activity;
    }

    @Override
    public Activity getActivityById(Long id) {
        Activity activity = activityRepoMysql.findById(id).orElseThrow();
        return activity;
    }

    @Override
    public Map<String, Object> getActivityDataJson(Long id, Client currentClient) {
        Map<String, Object> jsonData = new HashMap<>();
        Activity activity = getActivityById(id);

        Set<Activity> activities = new HashSet<>();
        activities.add(activity);
        jsonData.put("activity", createActivityJson(activity, currentClient));

        List<Material> materials = materialRepoMysql.getMaterialsByActivities_Id(id);
        jsonData.put("materials", materials);

        List<Tag> tags = tagRepoMysql.findByActivitiesIn(activities);
        jsonData.put("activity_tags", tags);

        tags = tagRepoMysql.findAll();
        jsonData.put("tags", tags);
        boolean isOwner = isOwner(currentClient, activity.getId());
        jsonData.put("isOwner", isOwner);

        Client activityCreator = clientService.getClientById(activity.getClient().getId());
        onwerPropertiesMap(activityCreator, jsonData);
        return jsonData;
    }

    private void onwerPropertiesMap(Client activityCreator, Map<String, Object> jsonData) {
        jsonData.put("ownerName", activityCreator.getName() + " " + activityCreator.getLastname());
        jsonData.put("ownerDescription", activityCreator.getDescription());
        jsonData.put("ownerEmail", activityCreator.getEmail());
        if (activityCreator.getClub() != null){
            jsonData.put("ownerClubName", activityCreator.getClub().getName());
        } else {
            jsonData.put("ownerClubName", "");
        }

        jsonData.put("ownerTotalLikes", getAllLikesOfActivivitiesOfUser(activityCreator));
        jsonData.put("ownerTotalActivities", activityCreator.getActivities().size());
    }

    private int getAllLikesOfActivivitiesOfUser(Client activityCreator) {
        int total = 0;
        for (Activity activity : activityCreator.getActivities()){
            total += activity.getHearts().size();
        }
        return total;
    }

    @Override
    public boolean isOwner(Client currentClient, Long activityId) {
        Activity activity = activityRepoMysql.findActivityByClientAndId(currentClient, activityId);
        if (activity != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepoMysql.findAll();
    }

    @Override
    public Map<String, Object> clientWithNewActivity(Client client, Map<String, Object> activityWithNewLike) {
        Map<String, Object> activityWithNewUser = new HashMap<>();
        String jwt = jwtProvider.generateToken(clientService.getClientInJsonFormat(client));
        activityWithNewUser.put("client", clientService.getClientInJsonFormat(client));
        activityWithNewUser.put("activity", activityWithNewLike);
        activityWithNewUser.put("jwt", jwt);
        return activityWithNewUser;
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
