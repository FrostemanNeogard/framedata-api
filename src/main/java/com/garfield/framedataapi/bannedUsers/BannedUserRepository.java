package com.garfield.framedataapi.bannedUsers;

import com.garfield.framedataapi.users.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.UUID;

public interface BannedUserRepository extends ListCrudRepository<BannedUser, UUID> {
    List<BannedUser> findAllByUser(User user);
}
