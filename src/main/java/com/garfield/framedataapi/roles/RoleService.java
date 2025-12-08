package com.garfield.framedataapi.roles;

import com.garfield.framedataapi.users.User;
import com.garfield.framedataapi.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRoles(role);
    }

}
