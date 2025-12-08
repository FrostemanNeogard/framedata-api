package com.garfield.framedataapi.framedata.dtos;

import com.garfield.framedataapi.framedata.FramedataIdentity;
import com.garfield.framedataapi.framedata.FramedataTemplate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateFramedataDto(
        @NotNull @Valid FramedataIdentity identity,
        @NotNull @Valid FramedataTemplate data) {
}
