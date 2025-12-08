package com.garfield.framedataapi.games;

import com.garfield.framedataapi.core.BaseEntity;
import com.garfield.framedataapi.framedata.Framedata;
import com.garfield.framedataapi.framedata.FramedataTemplate;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "games")
@NoArgsConstructor
@Getter
public class Game extends BaseEntity {

    @Column(unique = true)
    private String name;

    @Embedded
    private FramedataTemplate attributesTemplate;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<GameCharacter> gameCharacters;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<Framedata> framedata;

    public Game(String name, FramedataTemplate attributesTemplate) {
        this.name = name;
        this.attributesTemplate = attributesTemplate;
    }

    public Game(String name, Map<String, Object> attributesTemplate) {
        this.name = name;
        this.attributesTemplate = new FramedataTemplate(attributesTemplate);
    }

}
