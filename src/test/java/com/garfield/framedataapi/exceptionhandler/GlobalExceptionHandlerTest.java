package com.garfield.framedataapi.exceptionhandler;

import com.garfield.framedataapi.config.structure.ApiResponse;
import com.garfield.framedataapi.config.exceptionHandler.GlobalExceptionHandler;
import com.garfield.framedataapi.games.Game;
import com.garfield.framedataapi.games.exceptions.GameAlreadyExistsException;
import com.garfield.framedataapi.games.exceptions.GameNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleGameNotFoundException_withName_shouldReturn404AndCorrectMessage() {
        String gameName = "Tekken";
        GameNotFoundException ex = new GameNotFoundException(gameName);

        ResponseEntity<ApiResponse<String>> response = handler.handleGameNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(String.format("Game \"%s\" not found.", gameName), response.getBody().getError());
        assertNull(response.getBody().getData());
    }

    @Test
    void handleGameNotFoundException_withId_shouldReturn404AndCorrectMessage() {
        UUID gameId = UUID.randomUUID();
        GameNotFoundException ex = new GameNotFoundException(gameId);

        ResponseEntity<ApiResponse<String>> response = handler.handleGameNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(String.format("Game \"%s\" not found.", gameId), response.getBody().getError());
        assertNull(response.getBody().getData());
    }

    @Test
    void handleGameAlreadyExistsException_shouldReturn409AndCorrectMessage() {
        Game gameMock = new Game("Street Fighter");

        GameAlreadyExistsException ex = new GameAlreadyExistsException(gameMock);

        ResponseEntity<ApiResponse<String>> response = handler.handleGameAlreadyExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
        assertEquals("Game \"Street Fighter\" already exists.", response.getBody().getError());
        assertNull(response.getBody().getData());
    }

}
