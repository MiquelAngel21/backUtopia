package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.ClubWithAddressDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;

import java.util.Map;

public interface ClubService {
    Club createClub(ClubWithAddressDto clubWithAddressDto, Client toClient);

    Map<String, Object> getPaginatedClubs(int start, int length);
    String getClubNameByClient(Client client);
}
