package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.ClubWithAddressDto;
import com.utopiapp.demo.dto.DescriptionPetitionDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.Petition;

import java.util.Map;

public interface ClubService {
    Club createClub(ClubWithAddressDto clubWithAddressDto, Client toClient);

    Map<String, Object> getPaginatedClubs(String name, int start, int length);

    Petition createNewPetition(Long clubId, DescriptionPetitionDto descriptionPetitionDto, Client toClient);
}
