package com.utopiapp.demo.controllers;

import com.google.gson.Gson;
import com.utopiapp.demo.dto.GithubCodeDto;
import com.utopiapp.demo.dto.JwtDto;
import com.utopiapp.demo.dto.LoginDto;
import com.utopiapp.demo.dto.RegisterDto;
import com.utopiapp.demo.exceptions.OAuth2GitHubAuthenticationException;
import com.utopiapp.demo.jwt.JwtProvider;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.File;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ClientService;
import com.utopiapp.demo.service.interfaces.SettingsService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController

public class AuthorizationController{

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ClientService clientService;
    private final SettingsService settingsService;

    public AuthorizationController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, ClientService clientService, SettingsService settingsService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.clientService = clientService;
        this.settingsService = settingsService;
    }

    @PostMapping(value = "/register", produces = {"application/json"})
    @ResponseBody
    public Client register(@RequestBody RegisterDto registerDTO){
        return clientService.addClient(registerDTO);
    }

    @PostMapping("/login")
    public JwtDto login(@RequestBody LoginDto loginDTO){

        clientService.verifyLoginFormInformation(loginDTO);

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
                                loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserMain userMain = (UserMain) authentication.getPrincipal();
        Map<String, Object> currentUser = clientService.getClientInJsonFormat(clientService.getClientByEmail(userMain.getEmail()));
        String jwt = jwtProvider.generateToken(currentUser);
        return new JwtDto(jwt, currentUser);
    }

    @GetMapping(value = "/image/{id}")
    public ResponseEntity<byte[]> getImage(
            @PathVariable Long id
    ) {
        File file = clientService.getImageById(id);
        HttpHeaders httpHeaders = clientService.chooseImageType(file);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(file.getContent());
    }

    @GetMapping(value = "/isCoordinator", produces = {"application/json"})
    public Boolean getIsCoordinator(
            Authentication authentication
    ) {
        UserMain userMain = (UserMain) authentication.getPrincipal();
        return clientService.isCoordinator(userMain.toClient());
    }

    @GetMapping(value = "/profile-image/{id}", produces = {"application/json"})
    public File getProfileImage(
            @PathVariable Long id
    ) {
        File file = settingsService.getProfileImageByClient(id);
        return file;
    }

    @PostMapping(value = "/get-github-oauth2", produces = {"application/json"})
    public ResponseEntity<?> getOauth2(
            @RequestBody GithubCodeDto githubCodeDto
    ) {
        CloseableHttpClient client = HttpClients.createDefault();
        githubCodeDto.setClientSecret("ca667aa4e1170fa79539f467c0f5f24126e8c397");
        githubCodeDto.setClient_id("f753362bc5700df36a2e");
        Gson gson = new Gson();
        String res = clientService.postToGitHubWithOauth2Information(githubCodeDto, client);
        if (res.contains("error")){
            throw new OAuth2GitHubAuthenticationException();
        }

        String user = clientService.getToGitHub(res, client);
        Map<String, String> map = gson.<Map<String, String>>fromJson(user, Map.class);
        String username = map.get("login");
        String name = map.get("name");
        String email = map.get("email");

        Client clientFromUtopiWeb = clientService.getClientFromOauth2(username, name, email);
        Map<String, Object> clientJson = clientService.getClientInJsonFormat(clientFromUtopiWeb);
        String jwt = jwtProvider.generateToken(clientJson);
        return new ResponseEntity<>(new JwtDto(jwt, clientJson), HttpStatus.OK);
    }


}
