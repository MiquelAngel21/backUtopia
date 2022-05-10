package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.JwtDto;
import com.utopiapp.demo.dto.LoginDTO;
import com.utopiapp.demo.dto.RegisterDTO;
import com.utopiapp.demo.jwt.JwtProvider;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.service.interfaces.ClientService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public Client register(@RequestBody RegisterDTO registerDTO){
        return clientService.addClient(registerDTO);
    }

    @PostMapping("/login")
    public JwtDto login(@RequestBody LoginDTO loginDTO){

        clientService.verifyLoginFormInformation(loginDTO);

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
                                loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserMain userMain = (UserMain) authentication.getPrincipal();

        String jwt = jwtProvider.generateToken(userMain);

        return new JwtDto(jwt);
    }
}
