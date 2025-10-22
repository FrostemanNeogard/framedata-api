package com.garfield.framedataapi.games.exceptions;

import com.garfield.framedataapi.framedata.FramedataAttributes;

public class InvalidAttributesTemplateJsonException extends RuntimeException {

    public InvalidAttributesTemplateJsonException(FramedataAttributes attributesTemplateJson) {
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
