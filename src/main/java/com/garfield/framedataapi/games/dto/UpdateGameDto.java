package com.garfield.framedataapi.games.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateGameDto(
        @NotNull(message = "\"name\" field is required.")
        @Pattern(regexp = ".*\\S.*", message = "\"name\" must not be blank.") String name) {
}
