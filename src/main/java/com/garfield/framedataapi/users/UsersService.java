package com.garfield.framedataapi.users;

import com.garfield.framedataapi.bannedUsers.BannedUser;
import com.garfield.framedataapi.bannedUsers.BannedUsersService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final BannedUsersService bannedUsersService;

    public UsersService(UsersRepository userRepository, BannedUsersService bannedUsersService) {
        this.usersRepository = userRepository;
        this.bannedUsersService = bannedUsersService;
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

    public void banUser(User user, Date bannedUntil) {
        this.banUser(user, bannedUntil, null);
    }

    public void banUser(User user, Date bannedUntil, String reason) {
        this.bannedUsersService.createBannedUser(new BannedUser(user, bannedUntil, reason));
    }

}