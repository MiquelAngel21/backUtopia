package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.ClubWithAddressDto;
import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ClubService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
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
        return clubService.getPaginatedClubs(start, length);
    }
}
