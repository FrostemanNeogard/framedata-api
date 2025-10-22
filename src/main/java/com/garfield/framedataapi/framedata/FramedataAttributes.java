package com.garfield.framedataapi.framedata;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.garfield.framedataapi.games.exceptions.InvalidAttributesTemplateJsonException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FramedataAttributes {

    @NotNull(message = "Categories array must be present")
    @NotEmpty(message = "Categories array cannot be empty")
    private final List<String> categories;

    @NotNull(message = "Identifiers array must be present")
    @NotEmpty(message = "Identifiers array cannot be empty")
    private final List<String> identifiers;

    private final Map<String, Object> dynamicFields;

    public FramedataAttributes(Map<String, Object> dynamicFields) {
        if (dynamicFields.containsKey("categories")) {
            throw new InvalidAttributesTemplateJsonException("categories");
        }

        if (dynamicFields.containsKey("identifiers")) {
            throw new InvalidAttributesTemplateJsonException("identifiers");
        }

        this.categories = List.of("");
        this.identifiers = List.of("");
        this.dynamicFields = dynamicFields;
    }

    public FramedataAttributes() {
        this(new HashMap<>());
    }

    @JsonAnySetter
    public void add(String key, Object value) {
        dynamicFields.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getDynamicFields() {
        return dynamicFields;
    }
}
