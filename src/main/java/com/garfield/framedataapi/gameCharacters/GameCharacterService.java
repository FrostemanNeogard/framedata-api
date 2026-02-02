package com.garfield.framedataapi.gameCharacters;

import com.garfield.framedataapi.gameCharacters.exceptions.AmbiguousGameCharacterNameException;
import com.garfield.framedataapi.gameCharacters.exceptions.GameCharacterAlreadyExistsException;
import com.garfield.framedataapi.gameCharacters.exceptions.GameCharacterNotFoundException;
import com.garfield.framedataapi.games.exceptions.GameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameCharacterService {

    private final GameCharacterRepository gameCharacterRepository;

    public void createGameCharacter(GameCharacter gameCharacter) {
        Set<GameCharacter> existingCharacter = gameCharacter.getGame().getGameCharacters()
                .stream()
                .filter(gc -> gc.getName().equals(gameCharacter.getName())).collect(Collectors.toSet());

        if (!existingCharacter.isEmpty()) {
            throw new GameCharacterAlreadyExistsException(gameCharacter);
        }

        this.gameCharacterRepository.save(gameCharacter);
    }

    public GameCharacter getGameCharacterByIdentifier(String input) {
        Set<GameCharacter> gameCharacter;

        try {
            gameCharacter = Set
                    .of(getGameCharacterById(UUID.fromString(input)));
        } catch (IllegalArgumentException e) {
            gameCharacter = getGameCharactersByName(input);
        }

        if (gameCharacter.isEmpty()) {
            throw new GameCharacterNotFoundException(input);
        }

        if (gameCharacter.size() > 1) {
            throw new AmbiguousGameCharacterNameException(input);
        }

        return gameCharacter.iterator().next();
    }

    public GameCharacter getGameCharacterById(UUID id) throws GameNotFoundException {
        Optional<GameCharacter> gameCharacter = this.gameCharacterRepository.findById(id);

        if (gameCharacter.isEmpty()) {
            throw new GameCharacterNotFoundException(id);
        }

        return gameCharacter.get();
    }

    private Set<GameCharacter> getGameCharactersByName(String name) throws GameNotFoundException {
        Set<GameCharacter> gameCharacter = this.gameCharacterRepository.findAllByName(name);

        if (gameCharacter.isEmpty()) {
            throw new GameCharacterNotFoundException(name);
        }

        return gameCharacter;
    }

    public GameCharacter updateGameCharacterName(GameCharacter gameCharacter, String newName) {
        gameCharacter.setName(newName);
        gameCharacterRepository.save(gameCharacter);
        return gameCharacter;
    }

    public void deleteGameCharacter(GameCharacter gameCharacter) {
        this.gameCharacterRepository.delete(gameCharacter);
    }

}
