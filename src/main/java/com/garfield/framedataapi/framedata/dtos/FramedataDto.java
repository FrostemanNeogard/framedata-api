package com.garfield.framedataapi.framedata.dtos;

import com.garfield.framedataapi.framedata.Framedata;
import com.garfield.framedataapi.framedata.FramedataIdentity;
import com.garfield.framedataapi.framedata.FramedataTemplate;

import java.util.UUID;

public record FramedataDto(
        UUID id,
        FramedataIdentity identity,
        FramedataTemplate data
) {

    public FramedataDto(Framedata framedata) {
        this(framedata.getId(), framedata.getIdentity(), framedata.getData());
    }

}
