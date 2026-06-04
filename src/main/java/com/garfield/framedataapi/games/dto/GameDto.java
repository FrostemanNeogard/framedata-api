package com.garfield.framedataapi.games.dto;

import com.garfield.framedataapi.framedata.FramedataTemplate;
import com.garfield.framedataapi.games.Game;

import java.util.UUID;

public record GameDto(UUID id, String name, FramedataTemplate attributesTemplate) {

    public static GameDto fromEntity(Game game) {
        return new GameDto(game.getId(), game.getName(), game.getAttributesTemplate());
    }

}
