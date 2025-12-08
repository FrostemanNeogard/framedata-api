package com.garfield.framedataapi.framedata.dtos;

import com.garfield.framedataapi.framedata.Framedata;
import com.garfield.framedataapi.gameCharacters.dtos.GameCharacterDto;
import com.garfield.framedataapi.games.dto.GameDto;

import java.util.List;
import java.util.Set;

public record FramedataResponseDto(GameDto game, GameCharacterDto gameCharacter, List<FramedataDto> framedata) {

    public FramedataResponseDto(Set<Framedata> framedata) {
        this(
                GameDto.fromEntity(framedata.iterator().next().getGame()),
                GameCharacterDto.fromEntity(framedata.iterator().next().getGameCharacter()),
                framedata.stream().map(FramedataDto::new).toList()
        );
    }

    public FramedataResponseDto(Framedata framedata) {
        this(
                GameDto.fromEntity(framedata.getGame()),
                GameCharacterDto.fromEntity(framedata.getGameCharacter()),
                List.of(new FramedataDto(framedata))
        );
    }

}
