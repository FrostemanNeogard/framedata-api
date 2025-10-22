package com.garfield.framedataapi.framedata.dtos;

import com.garfield.framedataapi.framedata.Framedata;
import com.garfield.framedataapi.framedata.FramedataAttributes;
import com.garfield.framedataapi.gameCharacters.dtos.GameCharacterDto;

import java.util.UUID;

public record FramedataDto(UUID id, GameCharacterDto gameCharacter, FramedataAttributes attributes) {

    public static FramedataDto fromEntity(Framedata framedata) {
        return new FramedataDto(
                framedata.getId(),
                GameCharacterDto.fromEntity(framedata.getGameCharacter()),
                framedata.getAttributes()
        );
    }

}
