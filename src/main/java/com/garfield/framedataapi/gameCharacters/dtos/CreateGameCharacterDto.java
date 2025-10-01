package com.garfield.framedataapi.gameCharacters.dtos;

import com.garfield.framedataapi.config.validation.ValidUuid;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateGameCharacterDto(
        @NotBlank(message = "\"name\" field is required.") String name,
        @ValidUuid(message = "\"gameId\" field must be a valid UUID.") UUID gameId) {
}
