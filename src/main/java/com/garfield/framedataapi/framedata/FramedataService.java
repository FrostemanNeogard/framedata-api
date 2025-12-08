package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.framedata.exceptions.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    @Transactional
    public void createFramedata(Framedata framedata) {
        this.validateFramedataContainsOnlyStringsAndMatchesTemplate(framedata);
        this.framedataRepository.save(framedata);
    }

    private void validateFramedataContainsOnlyStringsAndMatchesTemplate(Framedata framedata) {
        this.validateOnlyEmptyStrings(framedata.getData().getAttributes());
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

    public void validateOnlyEmptyStrings(Map<?, ?> map) {
        this.validateNodeOnlyContainsEmptyStrings(map);
    }

    private void validateNodeOnlyContainsEmptyStrings(Object node) {
        switch (node) {
            case String s -> {
                if (!s.isEmpty()) {
                    throw new FramedataTemplateJsonInvalidFieldValueException(s);
                }
            }
            case Map<?, ?> map -> map.values().forEach(this::validateNodeOnlyContainsEmptyStrings);
            case List<?> list -> list.forEach(this::validateNodeOnlyContainsEmptyStrings);
            default -> throw new FramedataJsonInvalidFieldTypeException(
                    node.getClass().getSimpleName(),
                    node.getClass().getTypeName()
            );
        }
    }

    public void deleteFramedata(Framedata framedata) {
        this.framedataRepository.delete(framedata);
    }

}
