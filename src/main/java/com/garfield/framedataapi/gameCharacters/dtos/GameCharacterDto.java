package com.garfield.framedataapi.gameCharacters.dtos;

import com.garfield.framedataapi.gameCharacters.GameCharacter;

import java.util.UUID;

public record GameCharacterDto(UUID characterId, String characterName, UUID gameId, String gameName) {

    public static GameCharacterDto fromEntity(GameCharacter gameCharacter) {
        return new GameCharacterDto(
                gameCharacter.getId(),
                gameCharacter.getName(),
                gameCharacter.getGame().getId(),
                gameCharacter.getGame().getName()
        );
    }

}
