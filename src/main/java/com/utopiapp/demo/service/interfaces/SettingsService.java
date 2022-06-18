package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.SettingsDataDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.File;

import java.util.Map;

public interface SettingsService {

    Map<String, Object> getSettingsData(Client currentClient);

    Client updateDataClient(SettingsDataDto settingsDataDto, Client currentClient);

    File getProfileImageByClient(Long userId);
}
