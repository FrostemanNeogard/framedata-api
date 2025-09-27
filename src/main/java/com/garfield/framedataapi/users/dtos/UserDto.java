package com.garfield.framedataapi.users.dtos;

import com.garfield.framedataapi.roles.dtos.RoleDto;
import com.garfield.framedataapi.users.User;

import java.util.List;
import java.util.UUID;

public record UserDto(UUID id, String username, String email, List<RoleDto> roles) {

    public static List<UserDto> fromEntityList(List<User> users) {
        return users.stream().map(UserDto::fromEntity).toList();
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(RoleDto::fromEntity).toList()
        );
    }

}
