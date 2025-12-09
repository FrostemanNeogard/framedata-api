package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.framedata.exceptions.*;
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

    public void validateOnlyStrings(Map<?, ?> map) {
        this.validateNodeOnlyContainsEmptyStrings(map, false);
    }

    public void validateOnlyEmptyStrings(Map<?, ?> map) {
        this.validateNodeOnlyContainsEmptyStrings(map, true);
    }

    private void validateNodeOnlyContainsEmptyStrings(Object node, boolean shouldBeEmpty) {
        switch (node) {
            case String s -> {
                if (shouldBeEmpty && !s.isEmpty()) {
                    throw new FramedataTemplateJsonInvalidFieldValueException(s);
                }
            }
            case Map<?, ?> map ->
                    map.values().forEach(item -> validateNodeOnlyContainsEmptyStrings(item, shouldBeEmpty));
            case List<?> list -> list.forEach(entry -> validateNodeOnlyContainsEmptyStrings(entry, shouldBeEmpty));
            default -> throw new FramedataJsonInvalidFieldTypeException(
                    node.getClass().getSimpleName(),
                    node.getClass().getTypeName()
            );
        }
    }

    public void deleteFramedata(Framedata framedata) {
        this.framedataRepository.delete(framedata);
    }

    // TODO: Temporary method from old implementation
    // This should potentially be completely reworked
    public String formatNotation(String inputNotation, boolean removePlus) {
        String modifiedNotation = inputNotation.toLowerCase();

        if (!modifiedNotation.contains("fc")) {
            modifiedNotation = modifiedNotation.replaceAll("cd", "f,n,d,df");
        }

        modifiedNotation = modifiedNotation
                .replaceAll("#", ":")
                .replaceAll("\\.", "")
                .replaceAll(" ", "")
                .replaceAll("\\s*\\([^)]*\\)\\s*", "")
                .replaceAll("[\u200B-\u200D\uFEFF]", "")
                .replaceAll("h\\.", "in heat")
                .replaceAll("r\\.", "in rage")
                .replaceAll("backturned", "bt")
                .replaceAll("backturn", "bt")
                .replaceAll("debug", "b,db,d,df")
                .replaceAll("gs", "f,n,b,db,d,df,f")
                .replaceAll("wr", "f,f,f")
                .replaceAll("qcf", "d,df,f")
                .replaceAll("qcb", "d,db,b")
                .replaceAll("hcf", "b,db,f,df,f")
                .replaceAll("hcb", "f,df,d,db,d")
                .replaceAll("ewgf", "f,n,d,df:2")
                .replaceAll("electric", "f,n,d,df:2")
                .replaceAll("ewhf", "f,n,d,df:2")
                .replaceAll("heatsmash", "in heat 2+3")
                .replaceAll("heatburst", "2+3")
                .replaceAll("rageart", "in rage df+1+2");

        String[] parts = modifiedNotation.split("or");
        if (parts.length > 0) {
            modifiedNotation = parts[parts.length - 1];
        }

        modifiedNotation = modifiedNotation.replaceAll("[\\s,/()]", "");

        if (removePlus) {
            modifiedNotation = modifiedNotation.replaceAll("\\+", "");
        }

        return modifiedNotation;
    }

    // TODO: Temporary method from old implementation
    // This should be improved
    public Framedata getFramedataByInput(
            GameCharacter gameCharacter,
            String inputNotation) {

        Set<Framedata> characterFramedata = gameCharacter.getFramedata();

        return characterFramedata
                .stream()
                .filter(fd -> fd.getIdentity().getIdentifiers().contains(inputNotation))
                .findFirst().orElseThrow(() -> new FramedataNotFoundException(gameCharacter, inputNotation));
    }

    public Set<Framedata> getMostSimilarFramedataEntries(GameCharacter gameCharacter, String inputNotation) {
        // TODO: Implement
        return gameCharacter.getFramedata();
    }

}
