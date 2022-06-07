package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.dto.LoginDto;
import com.utopiapp.demo.dto.RegisterDto;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.File;
import com.utopiapp.demo.model.Heart;
import com.utopiapp.demo.model.Role;
import com.utopiapp.demo.repositories.mysql.ClientRepo;
import com.utopiapp.demo.repositories.mysql.FileRepo;
import com.utopiapp.demo.service.interfaces.ClientService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepo clientRepo;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;
    private final FileRepo fileRepo;

    public ClientServiceImpl(ClientRepo clientRepo, PasswordEncoder passwordEncoder, HttpSession session, FileRepo fileRepo) {
        this.clientRepo = clientRepo;
        this.passwordEncoder = passwordEncoder;
        this.session = session;
        this.fileRepo = fileRepo;
    }


    @Override
    public Client getClientByEmail(String email) {
        return clientRepo.findByEmail(email);
    }

    @Override
    public Client save(Client newClient) {
        return clientRepo.save(newClient);
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
    public Client addClient(RegisterDto registerDTO) {
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

        client = clientRepo.save(client);
        return client;
    }

    @Override
    public void verifyRegisterFormInformation(RegisterDto registerDto) {
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
    public void verifyLoginFormInformation(LoginDto loginDTO) {

    }

    @Override
    public Map<String, Object> getUserAttributes() {
        SecurityContext sc = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) sc.getAuthentication();
        OAuth2AuthenticatedPrincipal principal = token.getPrincipal();
        return principal.getAttributes();
    }

    @Override
    public List<Map<String, Object>> getListOfClientsInJsonFormat(Collection<Client> clients){

        List<Map<String, Object>> allClientsInJsonFormat = new ArrayList<>();

        for (Client client : clients){
            Map<String, Object> clientInJsonFormat = getClientInJsonFormat(client);
            allClientsInJsonFormat.add(clientInJsonFormat);
        }

        return allClientsInJsonFormat;
    }

    @Override
    public Map<String, Object> getClientInJsonFormat(Client client) {
        Map<String, Object> currentUserData = new HashMap<>();
        currentUserData.put("id", client.getId().toString());
        currentUserData.put("name", client.getName());
        currentUserData.put("username", client.getUsername());
        currentUserData.put("lastname", client.getLastname());
        currentUserData.put("email", client.getEmail());
        currentUserData.put("role", client.getRole());
        currentUserData.put("Hearts", heartsToJsonFormat(client.getHearts()));
        return currentUserData;
    }

    private Object heartsToJsonFormat(Set<Heart> hearts) {
        List<Map<String, Object>> heartsInJsonFormat = new ArrayList<>();

        for (Heart heart : hearts){
            Map<String, Object> jsonHeart = new HashMap<>();
            jsonHeart.put("id", heart.getId());
            jsonHeart.put("activity_id", heart.getActivity().getId());
            jsonHeart.put("client_id", heart.getClient().getId());
            heartsInJsonFormat.add(jsonHeart);
        }

        return heartsInJsonFormat;
    }

    @Override
    public File getImageById(Long id) {
        Optional<File> file = fileRepo.findById(id);
        return file.orElse(null);
    }

    @Override
    public HttpHeaders chooseImageType(File file) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (file.getMediaType().equals("image/png")){
            httpHeaders.setContentType(MediaType.IMAGE_PNG);
        } else if (file.getMediaType().equals("image/jpeg")){
            httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        } else {
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
        return httpHeaders;
    }
}
