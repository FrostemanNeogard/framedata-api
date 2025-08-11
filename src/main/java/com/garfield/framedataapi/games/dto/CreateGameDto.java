package com.garfield.framedataapi.games.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateGameDto(@NotBlank(message = "\"name\" field is required.") String name) {
}
