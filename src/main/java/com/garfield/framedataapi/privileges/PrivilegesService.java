package com.garfield.framedataapi.privileges;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegesService {

    private final PrivilegesRepository privilegesRepository;

    public PrivilegesService(PrivilegesRepository privilegesRepository) {
        this.privilegesRepository = privilegesRepository;
    }

    List<Privilege> getAllPrivileges() {
        return this.privilegesRepository.findAll();
    }

}
