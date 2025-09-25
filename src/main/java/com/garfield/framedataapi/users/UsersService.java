package com.garfield.framedataapi.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }

    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        if (user == null || user.getRoles() == null) {
            return Collections.emptyList();
        }
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                .collect(Collectors.toList());
    }
}