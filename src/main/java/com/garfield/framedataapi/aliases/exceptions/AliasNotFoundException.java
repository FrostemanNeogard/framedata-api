package com.garfield.framedataapi.aliases.exceptions;

import com.garfield.framedataapi.games.Game;

import java.util.UUID;

public class AliasNotFoundException extends RuntimeException {

    public AliasNotFoundException(UUID uuid) {
        super(String.format("Alias \"%s\" not found.", uuid));
    }

    public AliasNotFoundException(Game game, String name) {
        super(String.format("Alias \"%s\" not found for game \"%s\".", name, game.getName()));
    }

}
