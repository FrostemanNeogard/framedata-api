package com.garfield.framedataapi.games.exceptions;

import java.util.UUID;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String name) {
        super(String.format("Game \"%s\" not found.", name));
    }

    public GameNotFoundException(UUID id) {
        super(String.format("Game \"%s\" not found.", id.toString()));
    }
}
