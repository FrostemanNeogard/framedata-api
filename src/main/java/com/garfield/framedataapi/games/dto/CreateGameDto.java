package com.garfield.framedataapi.games.dto;

import com.garfield.framedataapi.framedata.FramedataAttributes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateGameDto(
        @NotBlank(message = "\"name\" field is required") String name,
        @NotEmpty(message = "\"attributesTemplate\" field is required") FramedataAttributes attributesTemplate) {
}
