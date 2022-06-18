package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.dto.Message;
import com.utopiapp.demo.jwt.JwtProvider;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Tag;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ActivityService;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.ClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

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
        return activityService.getActivitiesByUserAndMostRecentDate(userMain.toClient());
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
    public Map<String, Object> createActivity(
            @RequestBody ActivityDto activityDto,
            Authentication authentication
    ) {
        Activity activity = new Activity();
        UserMain userMain = (UserMain) authentication.getPrincipal();
        activity = activityService.createNewActivity(activity, activityDto, userMain, false);
        return activityService.createActivityJson(activity, userMain.toClient());
    }

    @GetMapping(value = "/activities/{id}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> getViewActivity(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Client client = userMain.toClient();
        Map<String, Object> viewActivityDataJson = activityService.getActivityDataJson(id, client);

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
        return activityService.clientWithNewActivity(userMain.toClient(), activityWithNewLike);
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
