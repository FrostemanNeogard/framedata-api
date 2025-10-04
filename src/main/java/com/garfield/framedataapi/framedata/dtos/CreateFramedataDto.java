package com.garfield.framedataapi.framedata.dtos;

import com.garfield.framedataapi.config.validation.ValidUuid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;
import java.util.UUID;

public record CreateFramedataDto(
        @ValidUuid(message = "\"characterId\" field must be a valid UUID.") UUID characterId,
        @NotEmpty(message = "\"attributes\"") Map<String, Object> attributes) {
}
