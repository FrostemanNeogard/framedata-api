package com.garfield.framedataapi.framedata.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public record UpdateFramedataDto(
        @Size(min = 1, message = "\"identifiers\" must contain at least one identifier.")
        List<@NotBlank String> identifiers,
        List<@NotBlank String> categories,
        Map<String, Object> attributes,
        @PositiveOrZero(message = "\"position\" must be zero or greater.") Integer position) {
}
