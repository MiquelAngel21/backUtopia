package com.utopiapp.demo.service.interfaces;

import com.utopiapp.demo.model.Client;

public interface ClientService {
   Client getClientByEmail(String email);
   Client save(Client newClient);
}
