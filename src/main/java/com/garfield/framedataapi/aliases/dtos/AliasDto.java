package com.garfield.framedataapi.aliases.dtos;

import com.garfield.framedataapi.aliases.Alias;
import com.garfield.framedataapi.gameCharacters.dtos.GameCharacterDto;
import com.garfield.framedataapi.games.dto.GameDto;

import java.util.UUID;

public record AliasDto(UUID id, String aliasName, GameCharacterDto character, GameDto game) {

    public static AliasDto fromEntity(Alias alias) {
        return new AliasDto(
                alias.getId(),
                alias.getAliasName(),
                GameCharacterDto.fromEntity(alias.getGameCharacter()),
                GameDto.fromEntity(alias.getGame())
        );
    }

}
