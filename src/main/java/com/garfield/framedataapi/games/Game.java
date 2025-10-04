package com.garfield.framedataapi.games;

import com.garfield.framedataapi.framedata.Framedata;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
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

    @Column(columnDefinition = "jsonb")
    private String attributesTemplate;

    @OneToMany(mappedBy = "game")
    private Set<GameCharacter> gameCharacters;

    @OneToMany(mappedBy = "game")
    private Set<Framedata> framedata;

    public Game(String name) {
        this.name = name;
    }

}
