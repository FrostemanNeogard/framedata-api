package com.garfield.framedataapi.privileges;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface PrivilegesRepository extends ListCrudRepository<Privilege, UUID> {
}
