package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.games.Game;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.Map;
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

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attributes;

    public Framedata(Game game, GameCharacter gameCharacter, Map<String, Object> attributes) {
        this.game = game;
        this.gameCharacter = gameCharacter;
        this.attributes = attributes;
    }

    public Framedata(GameCharacter gameCharacter, Map<String, Object> attributes) {
        this(gameCharacter.getGame(), gameCharacter, attributes);
    }

}
