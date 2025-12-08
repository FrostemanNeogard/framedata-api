package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.core.BaseEntity;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.games.Game;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aliases")
@Getter
@NoArgsConstructor
public class Alias extends BaseEntity {

    @Column
    private String aliasName;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "game_character_id")
    private GameCharacter gameCharacter;

    public Alias(String aliasName, GameCharacter gameCharacter) {
        this.aliasName = aliasName;
        this.game = gameCharacter.getGame();
        this.gameCharacter = gameCharacter;
    }

}
