package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.model.UserMain;
import com.utopiapp.demo.repositories.mysql.ClientRepoMysqlImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ClientRepoMysqlImpl clientRepoMysqlImpl;

    public UserDetailsServiceImpl(ClientRepoMysqlImpl clientRepoMysqlImpl) {
        this.clientRepoMysqlImpl = clientRepoMysqlImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepoMysqlImpl.findByEmail(email);
        return client.toUserMain();
    }
}
