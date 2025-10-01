package com.garfield.framedataapi.gameCharacters.dtos;

import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.games.dto.GameDto;

import java.util.UUID;

public record GameCharacterDto(UUID characterId, String characterName, GameDto game) {

    public static GameCharacterDto fromEntity(GameCharacter gameCharacter) {
        return new GameCharacterDto(
                gameCharacter.getId(),
                gameCharacter.getName(),
                GameDto.fromEntity(gameCharacter.getGame())
        );
    }

}
