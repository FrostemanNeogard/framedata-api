package com.garfield.framedataapi.games.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record CreateGameDto(
        @NotBlank(message = "\"name\" field is required") String name,
        @NotNull(message = "\"attributesTemplate\" field is required") Map<String, Object> attributesTemplate) {
}
