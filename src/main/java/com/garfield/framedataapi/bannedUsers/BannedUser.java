package com.garfield.framedataapi.bannedUsers;

import com.garfield.framedataapi.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "banned_users")
@NoArgsConstructor
@Getter
public class BannedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String reason;

    @Column
    @Temporal(TemporalType.DATE)
    private Date bannedAt;

    @Column
    @Temporal(TemporalType.DATE)
    private Date bannedUntil;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public BannedUser(User user, Date bannedUntil, String reason) {
        this.user = user;
        this.bannedAt = new Date(new Date().getTime());
        this.bannedUntil = bannedUntil;
        this.reason = reason;
    }

}
