package com.garfield.framedataapi.games;

import com.garfield.framedataapi.framedata.Framedata;
import com.garfield.framedataapi.framedata.FramedataAttributes;
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
    private FramedataAttributes attributesTemplate;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<GameCharacter> gameCharacters;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<Framedata> framedata;

    public Game(String name, FramedataAttributes attributesTemplate) {
        this.name = name;
        this.attributesTemplate = attributesTemplate;
    }

    public Game(String name, Map<String, Object> attributesTemplate) {
        this.name = name;
        this.attributesTemplate = new FramedataAttributes(attributesTemplate);
    }

}
