package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.ClubWithAddressDto;
import com.utopiapp.demo.dto.DescriptionPetitionDto;
import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.ClubService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ClubController {

    private final ClubService clubService;
    private final ClientService clientService;

    public ClubController(ClubService clubService, ClientService clientService) {
        this.clubService = clubService;
        this.clientService = clientService;
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
    public Map<String, Object> createNewPetition(
            @PathVariable Long clubId,
            @RequestBody DescriptionPetitionDto descriptionPetitionDto,
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return clubService.createNewPetition(clubId, descriptionPetitionDto, userMain.toClient());
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

    @GetMapping(value = "/club/{id}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> clubDetails(
            @PathVariable Long id
    ){
        return clubService.getClubById(id);
    }

    @GetMapping(value = "/club/coordinators/{clubId}", produces = {"application/json"})
    @ResponseBody
    public List<Map<String, Object>> coordinatorsOfThisClub(
            @PathVariable Long clubId
    ){
        return clubService.getCoordinatorsByClub(clubId);
    }

    @GetMapping(value = "/club/monitors/{clubId}", produces = {"application/json"})
    @ResponseBody
    public List<Map<String, Object>> monitorsOfThisClub(
            @PathVariable Long clubId
    ){
        return clubService.getMonitorsByClub(clubId);
    }

    @GetMapping(value = "/admin/club/{clientId}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> clubByCoordinator(
            @PathVariable Long clientId
    ){
        return clubService.getClubByCoordinator(clientId);
    }

    @GetMapping(value = "/admin/volunteers/{clubId}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object>  paginatedVolunteersByClub(
            @PathVariable Long clubId,
            @RequestParam int start,
            @RequestParam int length,
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return clientService.getPaginatedVolunteersByClub(null, userMain.toClient(), clubId, start, length);
    }

    @GetMapping(value = "/admin/petitions/{clubId}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object>  paginatedPetitionsByClub(
            @PathVariable Long clubId,
            @RequestParam int start,
            @RequestParam int length,
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return clubService.getPaginatedPetitionsByClub(userMain.toClient(), clubId, start, length);
    }

    @PostMapping(value = "/admin/petitions/{petitionId}/{newStatus}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> handlePetitions(
            @PathVariable Long petitionId,
            @PathVariable String newStatus,
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return clubService.acceptOrDenyPetitions(userMain.toClient(), newStatus, petitionId);
    }

    @PostMapping(value = "/admin/coordinators/{volunteerId}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object> ascentUser(
            @PathVariable Long volunteerId,
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return clubService.ascentOrLowerAVolunteer(userMain.toClient(), volunteerId);
    }

    @DeleteMapping(value = "/admin/volunteers/{volunteerId}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object>  deleteVolunteer(
            @PathVariable Long volunteerId,
            @RequestParam int start,
            @RequestParam int length,
            @RequestParam String volunteerSearcher,
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        clientService.deleteVolunteerFromClub(volunteerId);
        return clientService.getPaginatedVolunteersByClub(volunteerSearcher, userMain.toClient(), userMain.toClient().getCoordinator().getClub().getId(), start, length);
    }

    @PostMapping(value = "/admin/volunteers-filtered/{volunteerSearcher}", produces = {"application/json"})
    @ResponseBody
    public Map<String, Object>  getPaginatedFilteredVolunteers(
            @PathVariable String volunteerSearcher,
            @RequestParam int start,
            @RequestParam int length,
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return clientService.getPaginatedVolunteersByClub(volunteerSearcher, userMain.toClient(), userMain.toClient().getCoordinator().getClub().getId(), start, length);
    }
}
