package com.garfield.framedataapi.roles;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RolesRepository extends ListCrudRepository<Role, UUID> {
    Optional<Role> findByName(String name);
}
