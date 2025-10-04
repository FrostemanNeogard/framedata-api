package com.garfield.framedataapi.gameCharacters.exceptions;

public class AmbiguousGameCharacterNameException extends RuntimeException {

    public AmbiguousGameCharacterNameException(String name) {
        super(String.format("Character \"%s\" provided multiple results.", name));
    }

}
