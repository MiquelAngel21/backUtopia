package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.dto.Message;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Tag;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ActivityService;
import com.utopiapp.demo.service.interfaces.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class ActivityController {

    private final ActivityService activityService;
    private final TagService tagService;

    public ActivityController(ActivityService activityService, TagService tagService) {
        this.activityService = activityService;
        this.tagService = tagService;
    }

    @GetMapping(value = "/activities", produces = {"application/json"})
    @ResponseBody
    public List<Map<String, Object>> getActivities(){
        return activityService.getAllActivitiesByMostRecentDate();
    }

    @GetMapping(value = "/my-activities", produces = {"application/json"})
    @ResponseBody
    public List<Map<String, Object>> getAllMyActivities(
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.getActivitiesByUserAndMostRecentDate(userMain.getId());
    }

    @GetMapping(value = "/ranking", produces = {"application/json"})
    @ResponseBody
    public List<Map<String, Object>> getBestTopThree(){
        return activityService.getTopThreeActivitiesByRangeOfDates();
    }

    @PostMapping(value = "/new-activity", produces = {"application/json"})
    @ResponseBody
    public Activity createActivity(
            @RequestBody ActivityDto activityDto,
            Authentication authentication
    ){
        Activity activity = new Activity();
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.createNewActivity(activity, activityDto, userMain);
    }

    @PutMapping(value = "/update-activity/{id}", produces = {"application/json"})
    @ResponseBody
    public Activity updateActivity(
            @RequestBody ActivityDto activityDto,
            @PathVariable Long id,
            Authentication authentication
    ){
        Activity activity = new Activity();
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.updateAnExistingActivity(id, activity, activityDto, userMain);
    }

    @DeleteMapping(value = "/activity/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> deleteActivity(
            @PathVariable Long id
    ){
        activityService.deleteActivity(id);
        return new ResponseEntity<>(new Message("Activity deleted successfully"), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "create-activity", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> getCreateActivity(){
        try{
            List<Tag> tags = tagService.getAll();
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }
    }
}
