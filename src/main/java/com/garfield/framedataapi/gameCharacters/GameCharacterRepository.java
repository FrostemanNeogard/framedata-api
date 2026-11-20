package com.garfield.framedataapi.gameCharacters;

import com.garfield.framedataapi.games.Game;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Set;
import java.util.UUID;

public interface GameCharacterRepository extends ListCrudRepository<GameCharacter, UUID> {
    Set<GameCharacter> findAllByName(String name);

    Set<GameCharacter> findAllByGameAndNameIgnoreCase(Game game, String name);
}
