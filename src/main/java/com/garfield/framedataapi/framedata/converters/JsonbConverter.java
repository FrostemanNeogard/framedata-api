package com.garfield.framedataapi.framedata.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garfield.framedataapi.framedata.FramedataAttributes;
import com.garfield.framedataapi.framedata.exceptions.JsonFormatException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class JsonbConverter implements AttributeConverter<FramedataAttributes, String> {

    private final Logger logger = LoggerFactory.getLogger(JsonbConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(FramedataAttributes attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            this.logger.error("JSON writing error: {}", e.getMessage());
            throw new JsonFormatException(attribute.toString());
        }
    }

    @Override
    public FramedataAttributes convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, FramedataAttributes.class);
        } catch (JsonFormatException | JsonProcessingException e) {
            this.logger.error("JSON reading error: {}", e.getMessage());
            throw new JsonFormatException(dbData);
        }
    }
}
