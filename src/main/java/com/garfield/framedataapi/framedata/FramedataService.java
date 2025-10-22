package com.garfield.framedataapi.framedata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garfield.framedataapi.framedata.exceptions.*;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.games.exceptions.InvalidAttributesTemplateJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

    public void createFramedata(Framedata framedata) {
        validateAttributesAgainstTemplate(
                framedata.getAttributes(),
                framedata.getGame().getAttributesTemplate()
        );

        this.framedataRepository.save(framedata);
    }

    private void validateAttributesAgainstTemplate(FramedataAttributes attributes, FramedataAttributes templateJson) {
        try {
            JsonNode templateNode = objectMapper.valueToTree(templateJson);
            JsonNode dataNode = objectMapper.valueToTree(attributes);

            if (!templateNode.isObject()) {
                throw new InvalidAttributesTemplateJson(templateJson.toString());
            }

            if (!dataNode.isObject()) {
                throw new JsonFormatException(attributes.toString());
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
                    throw new InvalidAttributesTemplateJson(templateJson.toString());
                }
            }

        } catch (InvalidAttributesTemplateJson |
                 JsonFormatException |
                 FramedataJsonMissingRequiredFieldException |
                 FramedataJsonInvalidFieldTypeException e) {
            throw e;
        } catch (Exception e) {
            throw new UnknownInternalErrorException(e);
        }
    }

}
