package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.games.Game;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AliasesRepository extends ListCrudRepository<Alias, UUID> {
    Set<Alias> findAllByGameCharacter(GameCharacter gameCharacter);

    Optional<Alias> findByGameCharacterAndAliasName(GameCharacter gameCharacter, String aliasName);

    Optional<Alias> findByGameAndAliasName(Game game, String aliasName);
}
