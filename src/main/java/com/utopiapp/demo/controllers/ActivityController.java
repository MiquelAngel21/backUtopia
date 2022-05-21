package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.dto.Message;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Tag;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ActivityService;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.http.HttpResponse;
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
    public List<Map<String, Object>> getActivities() {
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
    public ResponseEntity<?> createActivity(
            @RequestBody ActivityDto activityDto,
            Authentication authentication,
            BindingResult bindingResult
    ){
        try {
        Activity activity = new Activity();
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Client currentUser = clientService.getClientByEmail(userMain.getEmail());

        if (activityDto.isEvent() && (!currentUser.getRole().equals("DIRECTOR"))){
            return new ResponseEntity<>(new Message("No tens permisos per crear events"), HttpStatus.CONFLICT);
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Message("Algún camp és incompleto o es incorrecto"), HttpStatus.BAD_REQUEST);
        }
        if (activityDto.getFiles().size() > 3) {
            return new ResponseEntity<>(new Message("Només pots adjuntar 3 fitxers com a màxim"), HttpStatus.BAD_REQUEST);
        }
        if (activityService.getActivityByName(activityDto.getName()) != null){
            return new ResponseEntity<>(new Message("Aquest nom d'activitat ja està en ús"), HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>(activityService.createNewActivity(activity, activityDto, userMain), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping(value = "/update-activity/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> updateActivity(
            @RequestBody ActivityDto activityDto,
            @PathVariable Long id,
            Authentication authentication,
            BindingResult bindingResult
    ){
        try {
            Activity activity = new Activity();
            UserMain userMain = (UserMain) authentication.getPrincipal();

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(new Message("Algún camp és incompleto o es incorrecto"), HttpStatus.BAD_REQUEST);
            }
            if (activityDto.getFiles().size() > 3) {
                return new ResponseEntity<>(new Message("Només pots adjuntar 3 fitxers com a màxim"), HttpStatus.BAD_REQUEST);
            }
            if (activityService.getActivityByName(activityDto.getName()) != null) {
                return new ResponseEntity<>(new Message("Aquest nom d'activitat ja està en ús"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(activityService.updateAnExistingActivity(id, activity, activityDto, userMain),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
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
    public ResponseEntity<?> getCreateActivity() {
        try {
            List<Tag> tags = tagService.getAll();
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }
    }
}
