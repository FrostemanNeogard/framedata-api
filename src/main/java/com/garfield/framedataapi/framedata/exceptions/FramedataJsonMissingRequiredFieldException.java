package com.garfield.framedataapi.framedata.exceptions;

public class FramedataJsonMissingRequiredFieldException extends RuntimeException {

    public FramedataJsonMissingRequiredFieldException(String requiredFieldName) {
        super(String.format("\"%s\" is a required field.", requiredFieldName));
    }

}
