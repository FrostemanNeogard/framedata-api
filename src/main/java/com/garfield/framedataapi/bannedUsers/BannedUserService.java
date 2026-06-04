package com.garfield.framedataapi.bannedUsers;

import com.garfield.framedataapi.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class BannedUserService {

    private final BannedUserRepository bannedUserRepository;

    public boolean isUserBanned(User user) {
        AtomicBoolean isBanned = new AtomicBoolean(false);

        this.bannedUserRepository.findAllByUser(user).forEach(bannedUser -> {
            if (bannedUser.getBannedUntil().after(new Date())) {
                isBanned.set(true);
            }
        });

        return isBanned.get();
    }

    public List<BannedUser> getAllBannedUsers() {
        return this.bannedUserRepository.findAll();
    }

    public void createBannedUser(BannedUser bannedUser) {
        this.bannedUserRepository.save(bannedUser);
    }

}
