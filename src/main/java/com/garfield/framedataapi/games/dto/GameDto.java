package com.garfield.framedataapi.games.dto;

import com.garfield.framedataapi.framedata.FramedataAttributes;
import com.garfield.framedataapi.games.Game;

import java.util.UUID;

public record GameDto(String name, UUID id, FramedataAttributes attributesTemplate) {

    public static GameDto fromEntity(Game game) {
        return new GameDto(game.getName(), game.getId(), game.getAttributesTemplate());
    }

}
