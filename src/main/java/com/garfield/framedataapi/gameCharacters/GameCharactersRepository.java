package com.garfield.framedataapi.gameCharacters;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Set;
import java.util.UUID;

public interface GameCharactersRepository extends ListCrudRepository<GameCharacter, UUID> {
    Set<GameCharacter> findAllByName(String name);
}
