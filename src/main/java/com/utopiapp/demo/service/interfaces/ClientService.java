package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.LoginDto;
import com.utopiapp.demo.dto.RegisterDto;
import com.utopiapp.demo.dto.SetingsDataDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.File;
import org.springframework.http.HttpHeaders;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ClientService {
   Client getClientByEmail(String email);
   Client getClientById(Long id);
   Client save(Client newClient);
   Client addClient(RegisterDto registerDTO);
   void verifyRegisterFormInformation(RegisterDto registerDto);
   void noRareCharactersInText(String text);
   void verifyLoginFormInformation(LoginDto loginDTO);
   Map<String, Object> getUserAttributes();
   void updateDataClient(SetingsDataDto setingsDataDto, Client currentClient);
   List<Map<String, Object>> getListOfClientsInJsonFormat(Collection<Client> clients);
   Map<String, Object> getClientInJsonFormat(Client client);
   File getImageById(Long id);
   HttpHeaders chooseImageType(File file);
   List<Client> getAllClientsByClub(Club club);

   Map<String, Object> getPaginatedVolunteersByClub(String filter, Client toClient, Long clubId, int start, int length);

    Boolean isCoordinator(Client client);

    void deleteVolunteerFromClub(Long volunteerId);
}
