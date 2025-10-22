package com.garfield.framedataapi.framedata.exceptions;

import java.util.Map;

public class JsonFormatException extends RuntimeException {

    public JsonFormatException(String invalidJson) {
        super(String.format("Invalid JSON format: \"%s\".", invalidJson));
    }

    public JsonFormatException(Map<String, Object> invalidJson) {
        this(invalidJson.toString());
    }

}
