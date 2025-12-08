package com.garfield.framedataapi.gameCharacters;

import com.garfield.framedataapi.aliases.Alias;
import com.garfield.framedataapi.core.BaseEntity;
import com.garfield.framedataapi.framedata.Framedata;
import com.garfield.framedataapi.games.Game;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "game_character")
@Getter
@NoArgsConstructor
public class GameCharacter extends BaseEntity {

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gameCharacter", cascade = CascadeType.ALL)
    private Set<Alias> aliases;

    @OneToMany(mappedBy = "gameCharacter", cascade = CascadeType.ALL)
    private Set<Framedata> framedata;

    public GameCharacter(String name, Game game) {
        this.name = name;
        this.game = game;
    }

}
