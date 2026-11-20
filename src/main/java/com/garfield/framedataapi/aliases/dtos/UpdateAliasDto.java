package com.garfield.framedataapi.aliases.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateAliasDto(
        @NotNull(message = "\"aliasName\" field is required.")
        @Pattern(regexp = ".*\\S.*", message = "\"aliasName\" must not be blank.") String aliasName) {
}
