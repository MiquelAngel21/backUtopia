package com.utopiapp.demo.service.implementations;

import com.utopiapp.demo.model.Role;
import com.utopiapp.demo.service.interfaces.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
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
}
