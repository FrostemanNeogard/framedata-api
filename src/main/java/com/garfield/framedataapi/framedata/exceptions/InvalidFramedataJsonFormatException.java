package com.garfield.framedataapi.framedata.exceptions;

import java.util.Map;

public class InvalidFramedataJsonFormatException extends RuntimeException {

    public InvalidFramedataJsonFormatException(String invalidJson) {
        super(String.format("Invalid JSON format: \"%s\".", invalidJson));
    }

    public InvalidFramedataJsonFormatException(Map<String, Object> invalidJson) {
        this(invalidJson.toString());
    }

}
