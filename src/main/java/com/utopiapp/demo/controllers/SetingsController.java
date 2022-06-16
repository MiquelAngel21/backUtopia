package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.SetingsDataDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.SettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "profile")
public class SetingsController {

    private final SettingsService settingsService;
    private final ClientService clientService;

    public SetingsController(SettingsService settingsService, ClientService clientService) {
        this.settingsService = settingsService;
        this.clientService = clientService;
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
    public ResponseEntity<?> updateSetings(@RequestBody SetingsDataDto setingsDataDto, Authentication authentication) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Client currentClient = userMain.toClient();
        clientService.updateDataClient(setingsDataDto, currentClient);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
