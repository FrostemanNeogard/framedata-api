package com.garfield.framedataapi.gameCharacters;

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
public class GameCharactersService {

    private final GameCharactersRepository gameCharactersRepository;

    public GameCharacter getGameCharacterById(UUID id) throws GameNotFoundException {
        Optional<GameCharacter> gameCharacter = this.gameCharactersRepository.findById(id);

        if (gameCharacter.isEmpty()) {
            throw new GameCharacterNotFoundException(id);
        }

        return gameCharacter.get();
    }

    public Set<GameCharacter> getGameCharactersByName(String name) throws GameNotFoundException {
        Set<GameCharacter> gameCharacter = this.gameCharactersRepository.findAllByName(name);

        if (gameCharacter.isEmpty()) {
            throw new GameCharacterNotFoundException(name);
        }

        return gameCharacter;
    }

    public void createGameCharacter(GameCharacter gameCharacter) {
        Set<GameCharacter> existingCharacter = gameCharacter.getGame().getGameCharacters()
                .stream()
                .filter(gc -> gc.getName().equals(gameCharacter.getName())).collect(Collectors.toSet());

        if (!existingCharacter.isEmpty()) {
            throw new GameCharacterAlreadyExistsException(gameCharacter);
        }

        this.gameCharactersRepository.save(gameCharacter);
    }
    
}
