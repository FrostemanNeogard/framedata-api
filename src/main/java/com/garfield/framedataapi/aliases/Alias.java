package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.games.Game;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "aliases")
@Getter
public class Alias {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String aliasName;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "game_character_id")
    private GameCharacter gameCharacter;

    public Alias(String aliasName, Game game, GameCharacter gameCharacter) {
        this.aliasName = aliasName;
        this.game = game;
        this.gameCharacter = gameCharacter;
    }

}
