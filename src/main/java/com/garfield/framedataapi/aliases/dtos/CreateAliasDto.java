package com.garfield.framedataapi.aliases.dtos;

import com.garfield.framedataapi.config.validation.ValidUuid;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateAliasDto(
        @NotBlank(message = "\"aliasName\" field is required.") String aliasName,
        @ValidUuid(message = "\"gameId\" field must be a valid UUID.") UUID gameId,
        @ValidUuid(message = "\"characterId\" field must be a valid UUID.") UUID characterId) {
}
