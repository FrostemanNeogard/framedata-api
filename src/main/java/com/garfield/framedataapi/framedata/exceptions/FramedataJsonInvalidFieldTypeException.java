package com.garfield.framedataapi.framedata.exceptions;

public class FramedataJsonInvalidFieldTypeException extends RuntimeException {

    public FramedataJsonInvalidFieldTypeException(String fieldName, String fieldTypeName) {
        super(String.format("\"%s\" has to be of type String. Found a type of \"%s\".", fieldName, fieldTypeName));
    }

}
