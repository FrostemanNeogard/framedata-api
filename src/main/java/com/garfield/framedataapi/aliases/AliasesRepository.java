package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.gameCharacters.GameCharacter;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Set;
import java.util.UUID;

public interface AliasesRepository extends ListCrudRepository<Alias, UUID> {
    Set<Alias> findAllByGameCharacter(GameCharacter gameCharacter);
}
