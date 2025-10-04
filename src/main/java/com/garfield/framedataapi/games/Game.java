package com.garfield.framedataapi.games;

import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.gameMetadata.GameMetadata;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "games")
@NoArgsConstructor
@Getter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "game")
    private Set<GameCharacter> gameCharacters;

    @OneToOne(mappedBy = "game")
    private GameMetadata gameMetadata;

    public Game(String name) {
        this.name = name;
    }

}
