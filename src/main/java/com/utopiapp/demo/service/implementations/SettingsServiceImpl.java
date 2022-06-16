package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.ClubService;
import com.utopiapp.demo.service.interfaces.SettingsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final ClientService clientService;
    private final ClubService clubService;

    public SettingsServiceImpl(ClientService clientService, ClubService clubService) {
        this.clientService = clientService;
        this.clubService = clubService;
    }

    @Override
    public Map<String, Object> getSettingsData(Client currentClient) {
        Map<String, Object> settingsData = new HashMap<>();
        settingsData.put("client", clientService.getClientInJsonFormat(currentClient));
        if (currentClient.getClub() != null){
            settingsData.put("club", clubService.convertClubToMap(currentClient.getClub()));
        } else {
            settingsData.put("club", new HashMap<>());
        }
        if (currentClient.getFile() != null){
            settingsData.put("file", clubService.fileToMap(currentClient.getFile()));
        } else {
            settingsData.put("file", new HashMap<>());
        }

        return settingsData;
    }
}
