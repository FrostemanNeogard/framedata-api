package com.garfield.framedataapi.games.dto;

import com.garfield.framedataapi.games.Game;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameDtoTest {

    @Test
    void fromEntity_createsDtoWithCorrectValues() {
        String name = "Tekken";
        UUID id = UUID.randomUUID();

        Game game = new Game(name);
        setId(game, id);

        GameDto dto = GameDto.fromEntity(game);

        assertEquals(name, dto.name());
        assertEquals(id, dto.id());
    }

    private void setId(Game game, UUID id) {
        try {
            var field = Game.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(game, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
