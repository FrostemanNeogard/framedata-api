package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.games.Game;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "framedata")
@Getter
@NoArgsConstructor
public class Framedata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private GameCharacter gameCharacter;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(columnDefinition = "jsonb")
    private String attributes;

    public Framedata(Game game, GameCharacter gameCharacter, String attributes) {
        this.game = game;
        this.gameCharacter = gameCharacter;
        this.attributes = attributes;
    }

    public Framedata(GameCharacter gameCharacter, String attributes) {
        this(gameCharacter.getGame(), gameCharacter, attributes);
    }

}
