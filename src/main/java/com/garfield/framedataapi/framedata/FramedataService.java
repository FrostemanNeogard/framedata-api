package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.framedata.dtos.UpdateFramedataDto;
import com.garfield.framedataapi.framedata.exceptions.*;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

        framedata.getGameCharacter().getFramedata().forEach(fd -> {
                fd.getIdentity().getIdentifiers().forEach(identifier -> {
                    if (framedata.getIdentity().getIdentifiers().contains(identifier)) {
                        throw new FramedataAlreadyExistsException(identifier);
                    }
                });
            });

        framedata.setPosition(this.nextPosition(framedata.getGameCharacter()));

        this.framedataRepository.save(framedata);
    }

    private int nextPosition(GameCharacter gameCharacter) {
        return gameCharacter.getFramedata().stream()
                .map(Framedata::getPosition)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .map(position -> position + 1)
                .orElse(0);
    }

    @Transactional
    public Framedata updateFramedata(Framedata framedata, UpdateFramedataDto dto) {
        if (dto.identifiers() != null) {
            this.validateIdentifiersAreUnique(framedata, dto.identifiers());
            framedata.getIdentity().setIdentifiers(dto.identifiers().stream().distinct().collect(Collectors.toList()));
        }

        if (dto.categories() != null) {
            framedata.getIdentity().setCategories(dto.categories().stream().distinct().collect(Collectors.toList()));
        }

        if (dto.attributes() != null) {
            Map<String, Object> merged = this.mergeAttributes(framedata.getData().getAttributes(), dto.attributes());

            this.validateOnlyStrings(merged);
            this.validateFramedataMatchesTemplate(merged, framedata.getGame().getAttributesTemplate().getAttributes());
            framedata.getData().setAttributes(merged);
        }

        if (dto.position() != null) {
            this.moveToPosition(framedata, dto.position());
        }

        return this.framedataRepository.save(framedata);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mergeAttributes(Map<String, Object> existing, Map<String, Object> updates) {
        Map<String, Object> merged = new HashMap<>(existing);

        updates.forEach((key, value) -> {
            Object current = merged.get(key);

            if (value instanceof Map && current instanceof Map) {
                merged.put(key, this.mergeAttributes((Map<String, Object>) current, (Map<String, Object>) value));
            } else {
                merged.put(key, value);
            }
        });

        return merged;
    }

    private void validateIdentifiersAreUnique(Framedata framedata, List<String> identifiers) {
        framedata.getGameCharacter().getFramedata().stream()
                .filter(other -> !other.getId().equals(framedata.getId()))
                .flatMap(other -> other.getIdentity().getIdentifiers().stream())
                .filter(identifiers::contains)
                .findFirst()
                .ifPresent(identifier -> {
                    throw new FramedataAlreadyExistsException(identifier);
                });
    }

    private void moveToPosition(Framedata framedata, int position) {
        List<Framedata> ordered = new ArrayList<>(framedata.getGameCharacter().getFramedata());
        ordered.removeIf(other -> other.getId().equals(framedata.getId()));
        ordered.add(Math.min(position, ordered.size()), framedata);

        for (int i = 0; i < ordered.size(); i++) {
            ordered.get(i).setPosition(i);
        }

        this.framedataRepository.saveAll(ordered);
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

            if (templateValue instanceof Map) {
                if (!(incomingValue instanceof Map)) {
                    throw new FramedataFieldShapeMismatchException(key, "an object");
                }

                validateFramedataMatchesTemplate(
                        (Map<String, Object>) incomingValue,
                        (Map<String, Object>) templateValue
                );
            } else if (templateValue instanceof List && !(incomingValue instanceof List)) {
                throw new FramedataFieldShapeMismatchException(key, "a list");
            } else if (templateValue instanceof String && !(incomingValue instanceof String)) {
                throw new FramedataFieldShapeMismatchException(key, "a string");
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
            case null -> throw new FramedataJsonInvalidFieldTypeException("null", "null");
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

    private record ScoredFramedata(Framedata framedata, double score) {
    }

    public List<Framedata> filterByCategory(List<Framedata> framedata, String category) {
        return framedata.stream()
                .filter(fd -> fd.getIdentity().getCategories() != null
                        && fd.getIdentity().getCategories().stream().anyMatch(category::equalsIgnoreCase))
                .toList();
    }

    public Framedata getFramedataByInput(GameCharacter gameCharacter, String inputNotation) {
        List<Framedata> characterFramedata = gameCharacter.getFramedata();

        String rawInput = inputNotation.replaceAll("[\u200B-\u200D\uFEFF]", "");
        Optional<Framedata> rawMatch = characterFramedata.stream()
                .filter(fd -> fd.getIdentity().getIdentifiers().contains(rawInput))
                .findFirst();

        if (rawMatch.isPresent()) {
            return rawMatch.get();
        }

        for (int i = 0; i < 2; i++) {
            boolean removePlus = (i > 0);
            String formattedInput = this.formatNotation(inputNotation, removePlus);

            for (Framedata moveData : characterFramedata) {
                List<String> identifiers = moveData.getIdentity().getIdentifiers();

                for (String identifier : identifiers) {
                    String formattedIdentifier = this.formatNotation(identifier, removePlus);

                    if (formattedInput.equals(formattedIdentifier)) {
                        return moveData;
                    }
                }
            }
        }

        throw new FramedataNotFoundException(gameCharacter, inputNotation);
    }

    public List<Framedata> getMostSimilarFramedataEntries(GameCharacter gameCharacter, String inputNotation) {
        List<Framedata> characterFramedata = gameCharacter.getFramedata();
        List<ScoredFramedata> similarityMap = new ArrayList<>();

        for (Framedata moveData : characterFramedata) {
            double bestScoreForMove = 0.0;

            for (String identifier : moveData.getIdentity().getIdentifiers()) {
                double score = this.calculateSimilarity(identifier, inputNotation);
                if (score > bestScoreForMove) {
                    bestScoreForMove = score;
                }
            }

            similarityMap.add(new ScoredFramedata(moveData, bestScoreForMove));
        }

        return similarityMap.stream()
                .sorted(Comparator.comparingDouble(ScoredFramedata::score).reversed())
                .map(ScoredFramedata::framedata)
                .limit(5)
                .toList();
    }

    private double calculateSimilarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
        }

        int distance = getLevenshteinDistance(longer, shorter);

        return (longerLength - distance) / (double) longerLength;
    }

    private int getLevenshteinDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= s1.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= s2.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        s1.charAt(i - 1) == s2.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[s2.length()];
    }

    public String formatNotation(String inputNotation, boolean removePlus) {
        if (inputNotation == null) {
            return "";
        }

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

}
