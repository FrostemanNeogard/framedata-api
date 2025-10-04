package com.garfield.framedataapi.framedata.dtos;

import com.garfield.framedataapi.framedata.Framedata;
import com.garfield.framedataapi.gameCharacters.dtos.GameCharacterDto;

import java.util.Map;
import java.util.UUID;

public record FramedataDto(UUID id, GameCharacterDto gameCharacter, Map<String, Object> attributes) {

    public static FramedataDto fromEntityAndAttributesMap(Framedata framedata, Map<String, Object> attributesMap) {
        return new FramedataDto(
                framedata.getId(),
                GameCharacterDto.fromEntity(framedata.getGameCharacter()),
                attributesMap
        );
    }

}
