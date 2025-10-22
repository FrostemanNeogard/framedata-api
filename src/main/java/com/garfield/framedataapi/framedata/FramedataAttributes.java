package com.garfield.framedataapi.framedata;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class FramedataAttributes {

    @NotNull(message = "Categories array must be present")
    @NotEmpty(message = "Categories array cannot be empty")
    private List<String> categories;

    @NotNull(message = "Identifiers array must be present")
    @NotEmpty(message = "Identifiers array cannot be empty")
    private List<String> identifiers;

    private final Map<String, Object> dynamicFields = new HashMap<>();

    @JsonAnySetter
    public void add(String key, Object value) {
        dynamicFields.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getDynamicFields() {
        return dynamicFields;
    }
}
