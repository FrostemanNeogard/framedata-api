package com.garfield.framedataapi.framedata.exceptions;

import com.garfield.framedataapi.framedata.Framedata;

public class FramedataAlreadyExistsException extends RuntimeException {

    public FramedataAlreadyExistsException(Framedata framedata) {
        super(String.format("Framedata already exists for attack: \"%s\".", framedata.getAttributes()));
    }

}
