package com.garfield.framedataapi.framedata.exceptions;

public class UnknownInternalErrorException extends RuntimeException {

    public UnknownInternalErrorException() {
        super("Something went wrong. Please try again later.");
    }

    public UnknownInternalErrorException(String message) {
        super(String.format("An unknown error occurred: \"%s\"", message));
    }

    public UnknownInternalErrorException(Exception e) {
        super(String.format("An unknown error occurred: \"%s\"", e.getMessage()));
    }

}
