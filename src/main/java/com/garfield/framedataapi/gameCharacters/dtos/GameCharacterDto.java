package com.garfield.framedataapi.gameCharacters.dtos;

import com.garfield.framedataapi.gameCharacters.GameCharacter;

import java.util.UUID;

public record GameCharacterDto(UUID id, String name) {

    public static GameCharacterDto fromEntity(GameCharacter gameCharacter) {
        return new GameCharacterDto(
                gameCharacter.getId(),
                gameCharacter.getName()
        );
    }

}
