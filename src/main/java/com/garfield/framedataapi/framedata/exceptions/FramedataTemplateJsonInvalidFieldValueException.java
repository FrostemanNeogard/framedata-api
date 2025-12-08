package com.garfield.framedataapi.framedata.exceptions;

public class FramedataTemplateJsonInvalidFieldValueException extends RuntimeException {

    public FramedataTemplateJsonInvalidFieldValueException(String fieldValue) {
        super(String.format("All template attribute fields must be empty. Found value: \"%s\".", fieldValue));
    }

}
