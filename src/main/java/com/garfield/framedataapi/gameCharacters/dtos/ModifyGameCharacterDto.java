package com.garfield.framedataapi.gameCharacters.dtos;

import jakarta.validation.constraints.NotBlank;

public record ModifyGameCharacterDto(@NotBlank String name) {
}
