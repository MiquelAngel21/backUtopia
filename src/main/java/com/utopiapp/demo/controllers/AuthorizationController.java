package com.utopiapp.demo.controllers;

import com.utopiapp.demo.dto.Message;
import com.utopiapp.demo.dto.RegisterDTO;
import com.utopiapp.demo.jwt.JwtProvider;
import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.service.implementations.RoleServiceImpl;
import com.utopiapp.demo.service.implementations.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
public class AuthorizationController{
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ClientServiceImpl clientService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    RoleServiceImpl roleService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(new Message("Algún camp és incorrecte"), HttpStatus.BAD_REQUEST);
        }
        if (clientService.getClientByEmail(registerDTO.getEmail()) != null){
            return new ResponseEntity<>(new Message("Aquest correu electrònic ja està en us"), HttpStatus.CONFLICT);
        }
        try {
            Client newClient = new Client(
                    registerDTO.getName(),
                    registerDTO.getUsername(),
                    registerDTO.getLastname(),
                    registerDTO.getEmail(),
                    passwordEncoder.encode(registerDTO.getPassword()),
                    LocalDateTime.now(),
                    roleService.chooseRole(registerDTO.getRole())
            );
            clientService.save(newClient);
            return new ResponseEntity<>(new Message("Usuari create"), HttpStatus.CREATED);
        } catch (Exception e ){
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }
}
