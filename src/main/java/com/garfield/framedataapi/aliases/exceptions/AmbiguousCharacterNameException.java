package com.garfield.framedataapi.aliases.exceptions;

public class AmbiguousCharacterNameException extends RuntimeException {

    public AmbiguousCharacterNameException(String name) {
        super(String.format("There were more than one character with name \"%s\".", name));
    }

}
