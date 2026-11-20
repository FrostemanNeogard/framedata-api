package com.garfield.framedataapi.framedata.exceptions;

import com.garfield.framedataapi.gameCharacters.GameCharacter;

import java.util.UUID;

public class FramedataNotFoundException extends RuntimeException {

    public FramedataNotFoundException(String identifier) {
        super(String.format("No framedata found with identifier: \"%s\".", identifier));
    }

    public FramedataNotFoundException(UUID id) {
        super(String.format("No framedata found with ID: \"%s\".", id));
    }

    public FramedataNotFoundException(GameCharacter gameCharacter, String input) {
        this(gameCharacter, "input", input);
    }

    public FramedataNotFoundException(GameCharacter gameCharacter, String field, String value) {
        super(String.format(
                "No framedata found for game: \"%s\" and character: \"%s\" with %s: \"%s\"",
                gameCharacter.getGame().getName(),
                gameCharacter.getName(),
                field,
                value
        ));
    }

}
