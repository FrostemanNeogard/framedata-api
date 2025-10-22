package com.garfield.framedataapi.framedata.exceptions;

public class JsonFormatException extends RuntimeException {

    public JsonFormatException(String invalidJson) {
        super(String.format("Invalid JSON format: \"%s\".", invalidJson));
    }

}
