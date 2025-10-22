package com.garfield.framedataapi.framedata.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.util.Map;
import java.util.UUID;

public record CreateFramedataDto(
        UUID characterId,
        @NotEmpty(message = "\"attributes\" field is required.") Map<String, Object> attributes) {
}
