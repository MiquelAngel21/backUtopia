package com.utopiapp.demo.controllers;

import com.utopiapp.demo.exceptions.EmptyFieldsException;
import com.utopiapp.demo.dto.Message;
import com.utopiapp.demo.dto.SetingsDataDto;
import com.utopiapp.demo.exceptions.IncorrectPasswordException;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ActivityService;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "profile")
public class SetingsController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ClientService clientService;

    @Autowired
    ClubService clubService;

    @Autowired
    ActivityService activityService;

    @GetMapping
    @ResponseBody
    public SetingsDataDto getSetings(Authentication authentication){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Client currentClient = userMain.toClient();
        String clubName = clubService.getClubNameByClient(currentClient);
        int countLikes = activityService.getNumberOfLikesByClient(currentClient);
        int countActivities = activityService.getNumberOfActivitiesByClient(currentClient);
        return new SetingsDataDto(currentClient.getName(), currentClient.getLastname(), currentClient.getEmail(), currentClient.getUsername(), clubName, countLikes, countActivities, currentClient.getDescription());
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<?> updateSetings(@RequestBody SetingsDataDto setingsDataDto, Authentication authentication){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Client currentClient = userMain.toClient();
        clientService.updateDataClient(setingsDataDto,currentClient);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
