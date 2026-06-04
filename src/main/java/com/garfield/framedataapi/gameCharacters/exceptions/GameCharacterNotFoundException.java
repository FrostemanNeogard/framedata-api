package com.garfield.framedataapi.gameCharacters.exceptions;

import java.util.UUID;

public class GameCharacterNotFoundException extends RuntimeException {

    public GameCharacterNotFoundException(String name) {
        super(String.format("Character \"%s\" not found.", name));
    }

    public GameCharacterNotFoundException(UUID id) {
        super(String.format("Character \"%s\" not found.", id.toString()));
    }

}
