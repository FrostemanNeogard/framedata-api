package com.garfield.framedataapi.games.dto;

import com.garfield.framedataapi.framedata.FramedataAttributes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateGameDto(
        @NotBlank(message = "\"name\" field is required") String name,
        @NotNull(message = "\"attributesTemplate\" field is required") FramedataAttributes attributesTemplate) {
}
