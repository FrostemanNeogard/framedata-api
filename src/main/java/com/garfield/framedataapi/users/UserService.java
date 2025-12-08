package com.garfield.framedataapi.users;

import com.garfield.framedataapi.bannedUsers.BannedUser;
import com.garfield.framedataapi.bannedUsers.BannedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BannedUserService bannedUserService;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
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
        this.bannedUserService.createBannedUser(new BannedUser(user, bannedUntil, reason));
    }

}