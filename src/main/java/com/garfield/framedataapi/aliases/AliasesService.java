package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.aliases.exceptions.AliasAlreadyExistsException;
import com.garfield.framedataapi.aliases.exceptions.AliasNotFoundException;
import com.garfield.framedataapi.games.Game;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AliasesService {

    private final AliasesRepository aliasesRepository;

    public AliasesService(AliasesRepository aliasesRepository) {
        this.aliasesRepository = aliasesRepository;
    }

    public void createAlias(Alias alias) {
        Optional<Alias> existingAlias = this.aliasesRepository.findByGameCharacterAndAliasName(
                alias.getGameCharacter(),
                alias.getAliasName()
        );

        if (existingAlias.isPresent()) {
            throw new AliasAlreadyExistsException(existingAlias.get());
        }

        this.aliasesRepository.save(alias);
    }

    public Alias getAliasByIdentifier(UUID uuid) {
        Optional<Alias> alias = this.aliasesRepository.findById(uuid);

        if (alias.isEmpty()) {
            throw new AliasNotFoundException(uuid);
        }

        return alias.get();
    }

    public Alias getAliasByIdentifier(Game game, String name) {
        Optional<Alias> alias = this.aliasesRepository.findByGameAndAliasName(game, name);

        if (alias.isEmpty()) {
            throw new AliasNotFoundException(game, name);
        }

        return alias.get();
    }

}
