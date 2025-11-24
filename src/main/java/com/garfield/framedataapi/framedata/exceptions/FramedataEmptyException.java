package com.garfield.framedataapi.framedata.exceptions;

public class FramedataEmptyException extends RuntimeException {

    public FramedataEmptyException() {
        super("Framedata may not be empty or null.");
    }

}
