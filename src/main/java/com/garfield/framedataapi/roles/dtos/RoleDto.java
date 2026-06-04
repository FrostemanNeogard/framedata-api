package com.garfield.framedataapi.roles.dtos;

import com.garfield.framedataapi.roles.Role;

import java.util.UUID;

public record RoleDto(UUID id, String name) {

    public static RoleDto fromEntity(Role role) {
        return new RoleDto(role.getId(), role.getName());
    }

}
