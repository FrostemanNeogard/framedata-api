package com.garfield.framedataapi.framedata.exceptions;

public class UnknownInternalErrorException extends RuntimeException {

    public UnknownInternalErrorException() {
        super("Something went wrong. Please try again later.");
    }

    public UnknownInternalErrorException(Exception e) {
        super(String.format("An unknown error occurred: \"%s\".", e.getMessage()));
    }

}
