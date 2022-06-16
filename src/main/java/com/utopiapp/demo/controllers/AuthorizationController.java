package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.JwtDto;
import com.utopiapp.demo.dto.LoginDto;
import com.utopiapp.demo.dto.RegisterDto;
import com.utopiapp.demo.jwt.JwtProvider;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.File;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ClientService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@RestController

public class AuthorizationController{

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ClientService clientService;

    public AuthorizationController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, ClientService clientService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.clientService = clientService;
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
}
