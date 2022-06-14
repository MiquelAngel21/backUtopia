package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.ClubWithAddressDto;
import com.utopiapp.demo.dto.DescriptionPetitionDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;

import java.util.List;
import java.util.Map;

public interface ClubService {
    Club createClub(ClubWithAddressDto clubWithAddressDto, Client toClient);
    Map<String, Object> getPaginatedClubs(String name, int start, int length);
    void createNewPetition(Long clubId, DescriptionPetitionDto descriptionPetitionDto, Client toClient);
    String getClubNameByClient(Client client);

    Map<String, Object> getClubById(Long id);

    List<Map<String, Object>> getCoordinatorsByClub(Long clubId);

    List<Map<String, Object>> getMonitorsByClub(Long clubId);
}
