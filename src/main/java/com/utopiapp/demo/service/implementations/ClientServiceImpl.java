package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.model.Client;
import com.utopiapp.demo.repositories.ClientRepoImpl;
import com.utopiapp.demo.service.interfaces.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepoImpl clientRepo;

    @Override
    public Client getClientByEmail(String email) {
        return clientRepo.findByEmail(email);
    }

    @Override
    public Client save(Client newClient) {
        return clientRepo.save(newClient);
    }
}
