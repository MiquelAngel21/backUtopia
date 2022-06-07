package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.LoginDto;
import com.utopiapp.demo.dto.RegisterDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.File;
import com.utopiapp.demo.model.Role;
import org.springframework.http.HttpHeaders;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClientService {
   Client getClientByEmail(String email);
   Client save(Client newClient);
   Role chooseRole(String role);
   Client addClient(RegisterDto registerDTO);
   void verifyRegisterFormInformation(RegisterDto registerDto);
   void noRareCharactersInText(String text);
   void verifyLoginFormInformation(LoginDto loginDTO);
   Map<String, Object> getUserAttributes();

   List<Map<String, Object>> getListOfClientsInJsonFormat(Collection<Client> clients);

   Map<String, Object> getClientInJsonFormat(Client client);

   File getImageById(Long id);

   HttpHeaders chooseImageType(File file);
}
