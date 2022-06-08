package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.ActivityDto;
import com.utopiapp.demo.dto.Message;
import com.utopiapp.demo.model.Activity;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Tag;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ActivityService;
import com.utopiapp.demo.service.interfaces.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ActivityController {

    private final ActivityService activityService;
    private final ClientService clientService;

    public ActivityController(ActivityService activityService, ClientService clientService) {
        this.activityService = activityService;
        this.clientService = clientService;
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
            Authentication authentication,
            BindingResult bindingResult
    ) {
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
            return new ResponseEntity<>(activityService.createNewActivity(activity, activityDto, userMain, false), HttpStatus.OK);
            if (activityDto.isEvent() && (!currentUser.getRole().equals("DIRECTOR"))) {
                return new ResponseEntity<>(new Message("No tens permisos per crear events"), HttpStatus.CONFLICT);
            }
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(new Message("Algún camp és incompleto o es incorrecto"), HttpStatus.BAD_REQUEST);
            }
            if (activityDto.getFiles().size() > 3) {
                return new ResponseEntity<>(new Message("Només pots adjuntar 3 fitxers com a màxim"), HttpStatus.BAD_REQUEST);
            }
            if (activityService.getActivityByName(activityDto.getName()) != null) {
                return new ResponseEntity<>(new Message("Aquest nom d'activitat ja està en ús"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(activityService.createNewActivity(activity, activityDto, userMain), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Error creant l'activitat"), HttpStatus.CONFLICT);
        }
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

        return viewActivityDataJson;
    }

    @PutMapping(value = "/activities/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> updateActivity(
            @RequestBody ActivityDto activityDto,
            @PathVariable Long id,
            Authentication authentication,
            BindingResult bindingResult
    ) {
        try {
            Activity activity = new Activity();
            UserMain userMain = (UserMain) authentication.getPrincipal();
            Client currentUser = clientService.getClientByEmail(userMain.getEmail());
            if (!activityService.isOwner(currentUser, activityDto.getId())) {
                return new ResponseEntity<>(new Message("No tens permisos per realitzar aquesta acció"), HttpStatus.UNAUTHORIZED);
            }
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(new Message("Algún camp és incompleto o es incorrecto"), HttpStatus.BAD_REQUEST);
            }
            if (activityService.getActivityByName(activityDto.getName()) != null) {
                return new ResponseEntity<>(new Message("Aquest nom d'activitat ja està en ús"), HttpStatus.BAD_REQUEST);
            }
            activityService.updateAnExistingActivity(id, activity, activityDto, userMain);
            return new ResponseEntity<>("Activitat editada correctament!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(value = "/activities/{id}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> deleteActivity(
            @PathVariable Long id
    ) {
        activityService.deleteActivity(id);
        return new ResponseEntity<>(new Message("Activity deleted successfully"), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
            (value = "new-activity", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> deleteActivity(@RequestBody ActivityDto activityDto, Authentication authentication) {
        try {
            UserMain userMain = (UserMain) authentication.getPrincipal();
            Client currentUser = clientService.getClientByEmail(userMain.getEmail());
            if (!activityService.isOwner(currentUser, activityDto.getId())) {
                return new ResponseEntity<>(new Message("No tens permisos per realitzar aquesta acció"), HttpStatus.UNAUTHORIZED);
            }
            activityService.deleteActivity(activityDto.getId());
            return new ResponseEntity<>(new Message("Activitat esborrada amb èxit!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Error esborrant l'activitat"), HttpStatus.BAD_REQUEST);
        }

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
