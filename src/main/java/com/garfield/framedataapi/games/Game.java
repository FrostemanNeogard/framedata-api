package com.garfield.framedataapi.games;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "games")
@NoArgsConstructor
@Getter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    public Game(String name) {
        this.name = name;
    }

}
