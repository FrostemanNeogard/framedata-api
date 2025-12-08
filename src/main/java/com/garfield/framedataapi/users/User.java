package com.garfield.framedataapi.users;

import com.garfield.framedataapi.core.BaseEntity;
import com.garfield.framedataapi.roles.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User extends BaseEntity {

    @Column
    private String username;

    @Column
    private String profilePictureUrl;

    @Column
    private String email;

    @Column
    @Temporal(TemporalType.DATE)
    private Date accountCreatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Collection<Role> roles;

}
