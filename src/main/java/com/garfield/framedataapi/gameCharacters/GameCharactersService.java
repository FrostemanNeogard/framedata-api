package com.garfield.framedataapi.gameCharacters;

import com.garfield.framedataapi.gameCharacters.exceptions.GameCharacterNotFoundException;
import com.garfield.framedataapi.games.exceptions.GameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class GameCharactersService {

    private final GameCharactersRepository gameCharactersRepository;

    public GameCharactersService(GameCharactersRepository gameCharactersRepository) {
        this.gameCharactersRepository = gameCharactersRepository;
    }

    public GameCharacter getGameCharactersById(UUID id) throws GameNotFoundException {
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
        this.gameCharactersRepository.save(gameCharacter);
    }
}
