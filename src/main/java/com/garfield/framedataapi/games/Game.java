package com.garfield.framedataapi.games;

import com.garfield.framedataapi.framedata.Framedata;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.Map;
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

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attributesTemplate;

    @OneToMany(mappedBy = "game")
    private Set<GameCharacter> gameCharacters;

    @OneToMany(mappedBy = "game")
    private Set<Framedata> framedata;

    public Game(String name, Map<String, Object> attributesTemplate) {
        this.name = name;
        this.attributesTemplate = attributesTemplate;
    }

}
