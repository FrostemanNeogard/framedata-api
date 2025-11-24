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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "character_id")
    private GameCharacter gameCharacter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @Embedded
    private FramedataIdentity identity;

    @Embedded
    private FramedataTemplate data;

    public Framedata(GameCharacter gameCharacter, FramedataIdentity identity, FramedataTemplate data) {
        this.gameCharacter = gameCharacter;
        this.identity = identity;
        this.data = data;
        this.game = gameCharacter.getGame();
    }

}
