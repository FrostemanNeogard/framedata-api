package com.garfield.framedataapi.framedata.exceptions;

import com.garfield.framedataapi.gameCharacters.GameCharacter;

public class FramedataNotFoundException extends RuntimeException {

    public FramedataNotFoundException(GameCharacter gameCharacter, String input) {
        super(String.format(
                "No framedata found for game: \"%s\" and character: \"%s\" with input: \"%s\"",
                gameCharacter.getGame().getName(),
                gameCharacter.getName(),
                input
        ));
    }

}
