package com.garfield.framedataapi.users;

import com.garfield.framedataapi.roles.Role;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends ListCrudRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    List<User> findByRoles(Role role);
}
