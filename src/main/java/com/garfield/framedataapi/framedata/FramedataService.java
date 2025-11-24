package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.framedata.exceptions.FramedataDoesNotMatchGameTemplateException;
import com.garfield.framedataapi.framedata.exceptions.FramedataEmptyException;
import com.garfield.framedataapi.framedata.exceptions.FramedataJsonInvalidFieldTypeException;
import com.garfield.framedataapi.framedata.exceptions.FramedataNotFoundException;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FramedataService {

    private final FramedataRepository framedataRepository;

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

    @Transactional
    public void createFramedata(Framedata framedata) {
        this.validateFramedataContainsOnlyStringsAndMatchesTemplate(framedata);
        this.framedataRepository.save(framedata);
    }

    private void validateFramedataContainsOnlyStringsAndMatchesTemplate(Framedata framedata) {
        this.validateOnlyStrings(framedata.getData().getAttributes());
        this.validateFramedataMatchesTemplate(framedata);
    }

    private void validateFramedataMatchesTemplate(Framedata framedata) {
        this.validateFramedataMatchesTemplate(
                framedata.getData().getAttributes(),
                framedata.getGame().getAttributesTemplate().getAttributes()
        );
    }

    private void validateFramedataMatchesTemplate(Map<String, Object> incoming, Map<String, Object> template) {
        if (incoming == null || incoming.isEmpty()) {
            throw new FramedataEmptyException();
        }

        for (String key : incoming.keySet()) {
            if (!template.containsKey(key)) {
                throw new FramedataDoesNotMatchGameTemplateException(key);
            }

            Object incomingValue = incoming.get(key);
            Object templateValue = template.get(key);

            if (incomingValue instanceof Map) {
                if (!(templateValue instanceof Map)) {
                    throw new FramedataDoesNotMatchGameTemplateException(key);
                }

                validateFramedataMatchesTemplate(
                        (Map<String, Object>) incomingValue,
                        (Map<String, Object>) templateValue
                );
            }
        }
    }

    public void validateOnlyStrings(List<?> list) {
        this.validateNodeOnlyContainsStrings(list);
    }

    public void validateOnlyStrings(Map<?, ?> map) {
        this.validateNodeOnlyContainsStrings(map);
    }

    public void validateOnlyStrings(String string) {
        this.validateNodeOnlyContainsStrings(string);
    }

    private void validateNodeOnlyContainsStrings(Object node) {
        switch (node) {
            case String ignored -> {
            }
            case Map<?, ?> map -> map.values().forEach(this::validateNodeOnlyContainsStrings);
            case List<?> list -> list.forEach(this::validateNodeOnlyContainsStrings);
            default -> throw new FramedataJsonInvalidFieldTypeException(
                    node.getClass().getSimpleName(),
                    node.getClass().getTypeName()
            );
        }
    }

}
