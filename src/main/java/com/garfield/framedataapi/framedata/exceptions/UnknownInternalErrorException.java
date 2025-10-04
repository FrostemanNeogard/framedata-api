package com.garfield.framedataapi.framedata.exceptions;

public class UnknownInternalErrorException extends RuntimeException {

    public UnknownInternalErrorException(Exception e) {
        super(String.format("An unknown error occurred: \"%s\".", e.getMessage()));
    }

}
