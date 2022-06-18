package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.JwtDto;
import com.utopiapp.demo.dto.Message;
import com.utopiapp.demo.dto.PasswordsSettingsDto;
import com.utopiapp.demo.dto.SettingsDataDto;
import com.utopiapp.demo.jwt.JwtProvider;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.ClubService;
import com.utopiapp.demo.service.interfaces.SettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/profile")
public class SetingsController {

    private final SettingsService settingsService;
    private final ClientService clientService;
    private final JwtProvider jwtProvider;

    public SetingsController(SettingsService settingsService, ClientService clientService, JwtProvider jwtProvider) {
        this.settingsService = settingsService;
        this.clientService = clientService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> getSettings(Authentication authentication) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Client currentClient = userMain.toClient();
        return settingsService.getSettingsData(currentClient);
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<?> updateSetings(@RequestBody SettingsDataDto settingsDataDto, Authentication authentication) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Client currentClient = clientService.getClientById(userMain.getId());
        currentClient = settingsService.updateDataClient(settingsDataDto, currentClient);
        Map<String, Object> currentClientMap = clientService.getClientInJsonFormat(currentClient);
        String token = jwtProvider.generateToken(currentClientMap);
        return new ResponseEntity<>(new JwtDto(token, currentClientMap),HttpStatus.OK);
    }

    @PutMapping(value = "/exit-club")
    @ResponseBody
    public ResponseEntity<?> exitClub(Authentication authentication){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Client currentClient = userMain.toClient();
        Club club = clientService.deleteVolunteerFromClub(currentClient.getId());
        if (club != null){
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            Map<String, String> clubDeleted = new HashMap<>();
            clubDeleted.put("success", "El club ha estat eliminat exitosament");
            return new ResponseEntity<>(clubDeleted, HttpStatus.CREATED);
        }
    }

    @PutMapping(value = "/signin-club")
    @ResponseBody
    public Map<String, String> signInClub(@RequestBody String accesCode, Authentication authentication){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Client currentClient = userMain.toClient();
        String clubName = clientService.signInClub(currentClient, accesCode);
        Map<String, String> response = new HashMap<>();
        response.put("clubName", clubName);
        return response;
    }

    @PutMapping(value = "/change-password")
    @ResponseBody
    public ResponseEntity<?> changePassword(
            @RequestBody PasswordsSettingsDto passwordsSettingsDto,
            Authentication authentication
    ){
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Client client = userMain.toClient();

        clientService.handlePasswords(client, passwordsSettingsDto);
        Map<String, String> message = new HashMap<>();

        message.put("modified", "Les contrasenyes s'han canviat correctament");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
