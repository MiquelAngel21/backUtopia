package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.ClubWithAddressDto;
import com.utopiapp.demo.dto.DescriptionPetitionDto;
import com.utopiapp.demo.dto.Message;
import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping(value = "/new-club", produces = {"application/json"})
    @ResponseBody
    public Club createNewClub(
            @RequestBody ClubWithAddressDto clubWithAddressDto,
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return clubService.createClub(clubWithAddressDto, userMain.toClient());
    }

    @GetMapping(value = "/clubs", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> allClubs(
            @RequestParam int start,
            @RequestParam int length
    ){
        return clubService.getPaginatedClubs(null, start, length);
    }

    @PostMapping(value = "/new-petition/{clubId}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<?> createNewPetition(
            @PathVariable Long clubId,
            @RequestBody DescriptionPetitionDto descriptionPetitionDto,
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        clubService.createNewPetition(clubId, descriptionPetitionDto, userMain.toClient());
        return new ResponseEntity<>(new Message("Petition created successfully"), HttpStatus.CREATED);
    }

    @GetMapping(value = "/clubs/{name}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> filteredClubsByName(
            @PathVariable String name,
            @RequestParam int start,
            @RequestParam int length
    ){
        return clubService.getPaginatedClubs(name, start, length);
    }
}
