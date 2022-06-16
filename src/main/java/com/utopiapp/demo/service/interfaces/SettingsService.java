package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.model.Client;

import java.util.Map;

public interface SettingsService {

    Map<String, Object> getSettingsData(Client currentClient);
}
