package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.LoginDto;
import com.utopiapp.demo.dto.RegisterDto;
import com.utopiapp.demo.dto.SetingsDataDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Role;

import java.util.Map;

public interface ClientService {
   Client getClientByEmail(String email);
   Client save(Client newClient);
   Role chooseRole(String role);
   Client addClient(RegisterDto registerDTO);
   void verifyRegisterFormInformation(RegisterDto registerDto);
   void noRareCharactersInText(String text);
   void verifyLoginFormInformation(LoginDto loginDTO);
   Map<String, Object> getUserAttributes();
   Map<String, Object> getClientOnJsonFormat(Client client);
   void updateDataClient(SetingsDataDto setingsDataDto, Client currentClient);
}
