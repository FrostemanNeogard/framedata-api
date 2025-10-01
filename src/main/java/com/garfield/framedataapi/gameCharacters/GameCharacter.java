package com.garfield.framedataapi.gameCharacters;

import com.garfield.framedataapi.aliases.Alias;
import com.garfield.framedataapi.games.Game;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "game_characters")
@Getter
@NoArgsConstructor
public class GameCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gameCharacter")
    private Set<Alias> aliases;

    public GameCharacter(String name, Game game) {
        this.name = name;
        this.game = game;
    }

}
