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

    @PostMapping(value = "/activities", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> createActivity(
            @Valid
            @RequestBody ActivityDto activityDto,
            Authentication authentication,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Message("Algún camp és incompleto o es incorrecto"), HttpStatus.BAD_REQUEST);
        }
        if (activityDto.getMaterials().size() > 3) {
            return new ResponseEntity<>(new Message("Només pots adjuntar 3 fitxers com a màxim"), HttpStatus.BAD_REQUEST);
        }
        try {
            UserMain userMain = (UserMain) authentication.getPrincipal();
            activityService.createNewActivity(activityDto, userMain);
            return new ResponseEntity<>(new Message("OK"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.CONFLICT);
        }
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
