package com.garfield.framedataapi.games.exceptions;

import com.garfield.framedataapi.framedata.FramedataTemplate;

public class InvalidAttributesTemplateJsonException extends RuntimeException {

    public InvalidAttributesTemplateJsonException(FramedataTemplate attributesTemplateJson) {
        super(String.format(
                "Attributes template was not valid: \"%s\". Please report this to an admin.",
                attributesTemplateJson.toString()
        ));
    }

    public InvalidAttributesTemplateJsonException(String disallowedFieldKey) {
        super(String.format(
                "Attributes template may not contain reserved key \"%s\".",
                disallowedFieldKey
        ));
    }

}
