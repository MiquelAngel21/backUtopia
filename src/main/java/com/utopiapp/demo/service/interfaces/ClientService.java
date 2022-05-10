package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.dto.LoginDTO;
import com.utopiapp.demo.dto.RegisterDTO;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Role;

import java.util.Map;

public interface ClientService {
   Client getClientByEmail(String email);
   Client save(Client newClient);
   Role chooseRole(String role);
   Client addClient(RegisterDTO registerDTO);
   void verifyRegisterFormInformation(RegisterDTO registerDto);
   void noRareCharactersInText(String text);
   void verifyLoginFormInformation(LoginDTO loginDTO);
   Map<String, Object> getUserAttributes();
}
