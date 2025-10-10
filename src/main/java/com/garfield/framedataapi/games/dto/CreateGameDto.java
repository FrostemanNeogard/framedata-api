package com.garfield.framedataapi.games.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;

public record CreateGameDto(
        @NotBlank(message = "\"name\" field is required.") String name,
        @NotEmpty(message = "\"attributesTemplate\" field is required.") Map<String, Object> attributesTemplate) {
}
