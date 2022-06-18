package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.ClubWithAddressDto;
import com.utopiapp.demo.dto.DescriptionPetitionDto;
import com.utopiapp.demo.model.*;
import com.utopiapp.demo.dto.FileDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.File;
import com.utopiapp.demo.model.Petition;

import java.util.List;
import java.util.Map;

public interface ClubService {
    Club createClub(ClubWithAddressDto clubWithAddressDto, Client toClient);
    Map<String, Object> getPaginatedClubs(String name, int start, int length);
    Map<String, Object> createNewPetition(Long clubId, DescriptionPetitionDto descriptionPetitionDto, Client toClient);

    Club findClubById(Long id);

    Address findAddressByClub(Club club);

    Map<String, Object> getClubById(Long id);

    List<Map<String, Object>> getCoordinatorsByClub(Long clubId);

    List<Map<String, Object>> getMonitorsByClub(Long clubId);

    Map<String, Object> getClubByCoordinator(Long clientId);

    Map<String, Object> getPaginatedPetitionsByClub(Client client, Long clubId, int start, int length);

    Map<String, Object> acceptOrDenyPetitions(Client toClient, String newStatus, Long petitionId);

    Map<String, Object> ascentOrLowerAVolunteer(Client toClient, Long volunteerId);

    Map<String, Object> convertClubToMap(Club club);

    Map<String, Object> convertAddressToMap(Address address);

    Map<String, Object> fileToMap(File file);

    File fileDtoIntoFile(FileDto fileDto);
}
