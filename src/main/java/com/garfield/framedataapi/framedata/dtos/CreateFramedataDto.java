package com.garfield.framedataapi.framedata.dtos;

import com.garfield.framedataapi.framedata.FramedataIdentity;
import com.garfield.framedataapi.framedata.FramedataTemplate;

import java.util.UUID;

public record CreateFramedataDto(
        UUID characterId,
        FramedataIdentity identity,
        FramedataTemplate attributes) {
}
