package com.garfield.framedataapi.games.exceptions;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameNotFoundExceptionTest {

    @Test
    void constructor_withName_shouldSetCorrectMessage() {
        String gameName = "Tekken";
        GameNotFoundException exception = new GameNotFoundException(gameName);

        String expectedMessage = String.format("Game \"%s\" not found.", gameName);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void constructor_withUUID_shouldSetCorrectMessage() {
        UUID gameId = UUID.randomUUID();
        GameNotFoundException exception = new GameNotFoundException(gameId);

        String expectedMessage = String.format("Game \"%s\" not found.", gameId);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
