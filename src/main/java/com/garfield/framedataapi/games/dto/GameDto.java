package com.garfield.framedataapi.games.dto;

import com.garfield.framedataapi.framedata.FramedataTemplate;
import com.garfield.framedataapi.games.Game;

import java.util.UUID;

public record GameDto(String name, UUID id, FramedataTemplate attributesTemplate) {

    public static GameDto fromEntity(Game game) {
        return new GameDto(game.getName(), game.getId(), game.getAttributesTemplate());
    }

}
