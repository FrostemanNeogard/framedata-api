package com.garfield.framedataapi.framedata.exceptions;

public class FramedataFieldShapeMismatchException extends RuntimeException {

    public FramedataFieldShapeMismatchException(String field, String expected) {
        super(String.format("\"%s\" must be %s, as defined by the game's template.", field, expected));
    }

}
