package com.garfield.framedataapi.framedata.exceptions;

import com.fasterxml.jackson.databind.node.JsonNodeType;

public class FramedataJsonInvalidFieldTypeException extends RuntimeException {

    public FramedataJsonInvalidFieldTypeException(String fieldName, JsonNodeType fieldType) {
        super(String.format("\"%s\" has to be of type \"%s\".", fieldName, fieldType));
    }

}
