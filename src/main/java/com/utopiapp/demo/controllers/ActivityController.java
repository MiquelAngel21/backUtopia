package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.dto.Message;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Tag;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ActivityService;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import com.utopiapp.demo.service.interfaces.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @Autowired
    ClientService clientService;

    @Autowired
    ClubService clubService;

    @GetMapping(value = "/activities", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> getActivities(
            @RequestParam int start,
            @RequestParam int length,
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.makePaginationWithDatabaseResults(userMain.toClient(), null, null, start, length);
    }

    @GetMapping(value = "/my-activities", produces = {"application/json"})
    @ResponseBody
    public List<Map<String, Object>> getAllMyActivities(
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.getActivitiesByUserAndMostRecentDate(userMain.getId());
    }

    @GetMapping(value = "/new-activity", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> getCreateActivity() {
        try {
            List<Tag> tags = activityService.getAllTags();
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/new-activity", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> createActivity(
            @RequestBody ActivityDto activityDto,
            Authentication authentication
    ) {

            Activity activity = new Activity();
            UserMain userMain = (UserMain) authentication.getPrincipal();
            return new ResponseEntity<>(activityService.createNewActivity(activity, activityDto, userMain, false), HttpStatus.OK);

    }

    @GetMapping(value = "/activities/{id}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> getViewActivity(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        List<Tag> tags = activityService.getAllTags();

        Map<String, Object> viewActivityDataJson = activityService.getActivityDataJson(id);
        viewActivityDataJson.put("tags", tags);

        Activity activity = activityService.getActivityById(id);
        boolean isOwner = activityService.isOwner(userMain.toClient(), activity.getId());
        viewActivityDataJson.put("isOwner", isOwner);

        Client activityCreator = clientService.getClientById(activity.getClient().getId());
        viewActivityDataJson.put("ownerName", activityCreator.getName() + " " + activityCreator.getLastname());
        viewActivityDataJson.put("ownerDescription", activityCreator.getDescription());
        viewActivityDataJson.put("ownerEmail", activityCreator.getEmail());
        String clubName = clubService.getClubNameByClient(activityCreator);
        viewActivityDataJson.put("ownerClubName", clubName);
        int countLikes = activityService.getNumberOfLikesByClient(activityCreator);
        viewActivityDataJson.put("ownerTotalLikes", countLikes);
        int countActivities = activityService.getNumberOfActivitiesByClient(activityCreator);
        viewActivityDataJson.put("ownerTotalActivities", countActivities);
        return viewActivityDataJson;
    }

    @PutMapping(value = "/activities/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> updateActivity(
            @RequestBody ActivityDto activityDto,
            @PathVariable Long id,
            Authentication authentication
    ) {
        try {
            Activity activity = new Activity();
            UserMain userMain = (UserMain) authentication.getPrincipal();
            activityService.updateAnExistingActivity(id, activity, activityDto, userMain);
            return new ResponseEntity<>("Activitat editada correctament!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(value = "/activities/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> deleteActivity(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        activityService.deleteActivity(id, userMain);
        return new ResponseEntity<>(new Message("Activitat esborrada amb èxit!"), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/manage-like/{id}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> likeManager(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Map<String, Object> activityWithNewLike = activityService.manageLike(id, userMain);
        Map<String, Object> hey = activityService.clientWithNewActivity(userMain.toClient(), activityWithNewLike);
        return hey;
    }


    @GetMapping(value = "/filter-activities/{filterText}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> filteredActivities(
            @PathVariable String filterText,
            @RequestParam int start,
            @RequestParam int length,
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.makePaginationWithDatabaseResults(userMain.toClient(), null, filterText, start, length);
    }

    @GetMapping(value = "/type/{type}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> typeOfActivitySearch(
            @PathVariable String type,
            @RequestParam int start,
            @RequestParam int length,
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.makePaginationWithDatabaseResults(userMain.toClient(), type, null, start, length);
    }

    @GetMapping(value = "/filter-activities/{searcher}/type/{type}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> typeOfActivitySearch(
            @PathVariable String searcher,
            @PathVariable String type,
            @RequestParam int start,
            @RequestParam int length,
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.makePaginationWithDatabaseResults(userMain.toClient(), type, searcher, start, length);
    }
}
