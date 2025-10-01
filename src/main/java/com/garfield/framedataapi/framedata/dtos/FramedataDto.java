package com.garfield.framedataapi.framedata.dtos;

import com.garfield.framedataapi.gameCharacters.dtos.GameCharacterDto;

import java.util.UUID;

public record FramedataDto(UUID id, GameCharacterDto gameCharacter, String attributes) {
}
