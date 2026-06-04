package com.garfield.framedataapi.framedata.exceptions;

public class FramedataDoesNotMatchGameTemplateException extends RuntimeException {

    public FramedataDoesNotMatchGameTemplateException(String fieldName) {
        super(String.format("\"%s\" is not an allowed field.", fieldName));
    }

}
