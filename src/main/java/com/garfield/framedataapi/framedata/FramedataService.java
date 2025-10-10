package com.garfield.framedataapi.framedata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garfield.framedataapi.framedata.exceptions.*;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.games.exceptions.InvalidAttributesTemplateJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FramedataService {

    private final FramedataRepository framedataRepository;
    private final ObjectMapper objectMapper;

    public Framedata getFramedataById(UUID id) {
        Optional<Framedata> framedata = this.framedataRepository.findById(id);

        if (framedata.isEmpty()) {
            throw new FramedataNotFoundException(id);
        }

        return framedata.get();
    }

    public Set<Framedata> getFramedataForCharacter(GameCharacter gameCharacter) {
        return this.framedataRepository.findAllByGameCharacter(gameCharacter);
    }

    public Map<String, Object> convertFramedataAttributeStringToMap(String attributes) {
        try {
            return this.objectMapper.readValue(attributes, Map.class);
        } catch (JsonProcessingException e) {
            throw new InvalidFramedataJsonFormatException(attributes);
        }
    }

    public String convertFramedataAttributeMapToString(Map<String, Object> attributes) {
        try {
            return this.objectMapper.writeValueAsString(attributes);
        } catch (JsonProcessingException e) {
            throw new InvalidFramedataJsonFormatException(attributes);
        }
    }

    public void createFramedata(Framedata framedata) {
        validateAttributesAgainstTemplate(
                framedata.getAttributes(),
                framedata.getGame().getAttributesTemplate()
        );

        this.framedataRepository.save(framedata);
    }

    private void validateAttributesAgainstTemplate(Map<String, Object> attributes, Map<String, Object> templateJson) {
        try {
            JsonNode templateNode = objectMapper.valueToTree(templateJson);
            JsonNode dataNode = objectMapper.valueToTree(attributes);

            if (!templateNode.isObject()) {
                throw new InvalidFramedataJsonFormatException(templateJson);
            }

            if (!dataNode.isObject()) {
                throw new InvalidFramedataJsonFormatException(attributes);
            }

            for (Iterator<String> it = templateNode.fieldNames(); it.hasNext(); ) {
                String fieldName = it.next();

                if (!dataNode.has(fieldName)) {
                    throw new FramedataJsonMissingRequiredFieldException(fieldName);
                }

                JsonNode templateField = templateNode.get(fieldName);
                JsonNode dataField = dataNode.get(fieldName);

                if (templateField.getNodeType() != dataField.getNodeType()) {
                    throw new FramedataJsonInvalidFieldTypeException(fieldName, dataField.getNodeType());
                }

                if (templateField.isObject()) {
                    throw new InvalidFramedataJsonFormatException(templateJson);
                }
            }

        } catch (InvalidAttributesTemplateJson |
                 InvalidFramedataJsonFormatException |
                 FramedataJsonInvalidFieldTypeException e) {
            throw e;
        } catch (Exception e) {
            throw new UnknownInternalErrorException(e);
        }
    }

}
