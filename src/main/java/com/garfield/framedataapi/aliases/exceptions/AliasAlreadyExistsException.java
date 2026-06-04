package com.garfield.framedataapi.aliases.exceptions;

import com.garfield.framedataapi.aliases.Alias;

public class AliasAlreadyExistsException extends RuntimeException {

    public AliasAlreadyExistsException(Alias alias) {
        super(String.format("Alias: \"%s\" already exists.", alias.getAliasName()));
    }

}
