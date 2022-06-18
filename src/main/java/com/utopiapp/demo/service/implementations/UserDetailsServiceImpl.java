package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.repositories.mysql.ClientRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ClientRepo clientRepo;

    public UserDetailsServiceImpl(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepo.findByEmail(email);
        if (client == null){
            client = clientRepo.findClientByUsername(email);
        }

        return client.toUserMain();
    }
}
