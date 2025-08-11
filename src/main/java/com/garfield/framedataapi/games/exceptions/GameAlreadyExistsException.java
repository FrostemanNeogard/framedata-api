package com.garfield.framedataapi.games.exceptions;

import com.garfield.framedataapi.games.Game;

public class GameAlreadyExistsException extends RuntimeException {
    public GameAlreadyExistsException(Game game) {
        super(String.format("Game \"%s\" already exists.", game.getName()));
    }
}
