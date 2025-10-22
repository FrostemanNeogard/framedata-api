package com.garfield.framedataapi.framedata.dtos;

import com.garfield.framedataapi.framedata.FramedataAttributes;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record CreateFramedataDto(
        UUID characterId,
        @NotEmpty(message = "\"attributes\" field is required") FramedataAttributes attributes) {
}
