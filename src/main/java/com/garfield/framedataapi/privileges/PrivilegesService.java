package com.garfield.framedataapi.privileges;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegesService {

    private final PrivilegesRepository privilegesRepository;

    List<Privilege> getAllPrivileges() {
        return this.privilegesRepository.findAll();
    }

}
