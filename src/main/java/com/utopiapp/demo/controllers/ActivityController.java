package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.dto.Message;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Tag;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ActivityService;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ActivityController {

    private final ActivityService activityService;
    private final TagService tagService;
    private final ClientService clientService;

    public ActivityController(ActivityService activityService, TagService tagService, ClientService clientService) {
        this.activityService = activityService;
        this.tagService = tagService;
        this.clientService = clientService;
    }

    @GetMapping(value = "/activities", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> getActivities(
            @RequestParam int start,
            @RequestParam int length
    ) {
        return activityService.makePaginationWithDatabaseResults(null, start, length);
    }

    @GetMapping(value = "/my-activities", produces = {"application/json"})
    @ResponseBody
    public List<Map<String, Object>> getAllMyActivities(
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.getActivitiesByUserAndMostRecentDate(userMain.getId());
    }

    @GetMapping(value = "/ranking", produces = {"application/json"})
    @ResponseBody
    public List<Map<String, Object>> getBestTopThree() {
        return activityService.getTopThreeActivitiesByRangeOfDates();
    }

    @PostMapping(value = "/new-activity", produces = {"application/json"})
    @ResponseBody
    public Activity createActivity(
            @RequestBody ActivityDto activityDto,
            Authentication authentication
    ) {
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
    ) {

        Activity activity = new Activity();
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.updateAnExistingActivity(id, activity, activityDto, userMain);
    }

    @DeleteMapping(value = "/activity/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> deleteActivity(
            @PathVariable Long id
    ) {
        activityService.deleteActivity(id);
        return new ResponseEntity<>(new Message("Activity deleted successfully"), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "new-activity", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> getCreateActivity() {
        try {
            List<Tag> tags = tagService.getAll();
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/activities/{id}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> getActivity(
            @PathVariable Long id
    ) {
        return activityService.getOneActivityById(id);
    }

    @GetMapping(value = "/manage-like/{id}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> likeManager(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return activityService.manageLike(id, userMain);
    }

    @GetMapping(value = "/filter-activities/{filterText}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> filteredActivities(
            @PathVariable String filterText,
            @RequestParam int start,
            @RequestParam int length
    ) {
        return activityService.makePaginationWithDatabaseResults(filterText, start, length);
    }
}
