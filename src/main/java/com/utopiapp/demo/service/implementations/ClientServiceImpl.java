package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.dto.LoginDTO;
import com.utopiapp.demo.dto.RegisterDTO;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.Role;
import com.utopiapp.demo.repositories.mysql.ClientRepoMysqlImpl;
import com.utopiapp.demo.service.interfaces.ClientService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepoMysqlImpl clientRepoMysqlImpl;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;

    public ClientServiceImpl(ClientRepoMysqlImpl clientRepoMysqlImpl, PasswordEncoder passwordEncoder, HttpSession session) {
        this.clientRepoMysqlImpl = clientRepoMysqlImpl;
        this.passwordEncoder = passwordEncoder;
        this.session = session;
    }


    @Override
    public Client getClientByEmail(String email) {
        return clientRepoMysqlImpl.findByEmail(email);
    }

    @Override
    public Client save(Client newClient) {
        return clientRepoMysqlImpl.save(newClient);
    }

    @Override
    public Role chooseRole(String role) {
        switch (role){
            case "user":
                return Role.USER;
            case "monitor":
                return Role.MONITOR;
            case "director":
                return Role.DIRECTOR;
        }
        return null;
    }

    @Override
    public Client addClient(RegisterDTO registerDTO) {
        verifyRegisterFormInformation(registerDTO);
        Client client = new Client(
                registerDTO.getName(),
                registerDTO.getUsername(),
                registerDTO.getLastname(),
                registerDTO.getEmail(),
                passwordEncoder.encode(registerDTO.getPassword()),
                LocalDateTime.now(),
                Role.USER
        );

        client = clientRepoMysqlImpl.save(client);
        return client;
    }

    @Override
    public void verifyRegisterFormInformation(RegisterDTO registerDto) {
        noRareCharactersInText(registerDto.getEmail());
        noRareCharactersInText(registerDto.getPassword());
        noRareCharactersInText(registerDto.getLastname());
        noRareCharactersInText(registerDto.getUsername());
        noRareCharactersInText(registerDto.getName());
        if (registerDto.getEmail().isEmpty() || registerDto.getPassword().isEmpty() ||
                registerDto.getLastname().isEmpty() || registerDto.getName().isEmpty() || registerDto.getUsername().isEmpty()){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void noRareCharactersInText(String text){
        String newText = text.replaceAll("['*\\-\"\\\\/\\[\\]?¿!¡<>=]*", "");
        if (!newText.equals(text)){
            throw new RuntimeException("No rare characters");
        }
    }

    @Override
    public void verifyLoginFormInformation(LoginDTO loginDTO) {

    }

    @Override
    public Map<String, Object> getUserAttributes() {
        SecurityContext sc = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) sc.getAuthentication();
        OAuth2AuthenticatedPrincipal principal = token.getPrincipal();
        return principal.getAttributes();
    }
}