package com.garfield.framedataapi.framedata.dtos;

import com.garfield.framedataapi.framedata.FramedataIdentity;
import com.garfield.framedataapi.framedata.FramedataTemplate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateFramedataDto(
        @NotNull UUID characterId,
        @NotNull @Valid FramedataIdentity identity,
        @NotNull @Valid FramedataTemplate data) {
}
