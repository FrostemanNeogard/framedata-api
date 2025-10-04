package com.garfield.framedataapi.games.exceptions;

public class InvalidAttributesTemplateJson extends RuntimeException {

    public InvalidAttributesTemplateJson(String attributesTemplateJson) {
        super(String.format(
                "Attributes template was not valid: \"%s\". Please report this to an admin.",
                attributesTemplateJson
        ));
    }

}
