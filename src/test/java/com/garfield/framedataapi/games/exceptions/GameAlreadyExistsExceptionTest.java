package com.garfield.framedataapi.games.exceptions;

import com.garfield.framedataapi.games.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameAlreadyExistsExceptionTest {

    @Test
    void constructor_shouldSetCorrectMessage() {
        String gameName = "Tekken";
        Game game = new Game(gameName);

        GameAlreadyExistsException exception = new GameAlreadyExistsException(game);

        String expectedMessage = String.format("Game \"%s\" already exists.", gameName);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
