package com.garfield.framedataapi.framedata.exceptions;

import com.garfield.framedataapi.gameCharacters.GameCharacter;

import java.util.UUID;

public class FramedataNotFoundException extends RuntimeException {

    public FramedataNotFoundException(UUID id) {
        super(String.format("No framedata found for character: \"%s\".", id));
    }

    public FramedataNotFoundException(GameCharacter gameCharacter, String input) {
        super(String.format(
                "No framedata found for game: \"%s\" and character: \"%s\" with input: \"%s\"",
                gameCharacter.getGame().getName(),
                gameCharacter.getName(),
                input
        ));
    }

}
