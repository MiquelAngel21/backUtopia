package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.GithubCodeDto;
import com.utopiapp.demo.dto.LoginDto;
import com.utopiapp.demo.dto.PasswordsSettingsDto;
import com.utopiapp.demo.dto.RegisterDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Club;
import com.utopiapp.demo.model.File;
import org.apache.http.impl.client.CloseableHttpClient;
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
   List<Map<String, Object>> getListOfClientsInJsonFormat(Collection<Client> clients);
   Map<String, Object> getClientInJsonFormat(Client client);
   File getImageById(Long id);
   HttpHeaders chooseImageType(File file);
   List<Client> getAllClientsByClub(Club club);
   void removeClubFromClient(Client client);
   String signInClub(Client client, String code);
   Map<String, Object> getPaginatedVolunteersByClub(String filter, Client toClient, Long clubId, int start, int length);
   Boolean isCoordinator(Client client);
   Club deleteVolunteerFromClub(Long volunteerId);

   Client getClientByUsername(String username);

   Client getClientFromOauth2(String username, String name, String email);

   String getToGitHub(String res, CloseableHttpClient client) ;

   String postToGitHubWithOauth2Information(GithubCodeDto githubCodeDto, CloseableHttpClient client) ;

   Long createUserId(Object idString, String username);

   void handlePasswords(Client client, PasswordsSettingsDto passwordsSettingsDto);
}
