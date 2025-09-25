package com.garfield.framedataapi.roles;

import com.garfield.framedataapi.users.User;
import com.garfield.framedataapi.users.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;
    private final UsersRepository usersRepository;

    public RolesService(RolesRepository rolesRepository, UsersRepository usersRepository) {
        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<Role> findByName(String name) {
        return rolesRepository.findByName(name);
    }

    public List<User> getUsersByRole(Role role) {
        return usersRepository.findByRoles(role);
    }

}
