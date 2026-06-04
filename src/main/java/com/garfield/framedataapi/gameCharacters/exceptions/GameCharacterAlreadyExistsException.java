package com.garfield.framedataapi.gameCharacters.exceptions;

import com.garfield.framedataapi.gameCharacters.GameCharacter;

public class GameCharacterAlreadyExistsException extends RuntimeException {

    public GameCharacterAlreadyExistsException(GameCharacter character) {
        super(String.format(
                "Character \"%s\" already exists in \"%s\".",
                character.getName(),
                character.getGame().getName())
        );
    }

}
