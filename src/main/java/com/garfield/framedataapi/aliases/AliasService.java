package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.aliases.exceptions.AliasAlreadyExistsException;
import com.garfield.framedataapi.aliases.exceptions.AliasNotFoundException;
import com.garfield.framedataapi.games.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AliasService {

    private final AliasRepository aliasRepository;

    public void createAlias(Alias alias) {
        Optional<Alias> existingAlias = this.aliasRepository.findByGameCharacterAndAliasName(
                alias.getGameCharacter(),
                alias.getAliasName()
        );

        if (existingAlias.isPresent()) {
            throw new AliasAlreadyExistsException(existingAlias.get());
        }

        this.aliasRepository.save(alias);
    }

    public Alias getAliasByGameAndIdentifier(Game game, String identifier) throws AliasNotFoundException {
        try {
            return getAliasById(UUID.fromString(identifier));
        } catch (IllegalArgumentException e) {
            return getAliasByName(game, identifier);
        }
    }

    public Alias getAliasById(UUID uuid) {
        Optional<Alias> alias = this.aliasRepository.findById(uuid);

        if (alias.isEmpty()) {
            throw new AliasNotFoundException(uuid);
        }

        return alias.get();
    }

    private Alias getAliasByName(Game game, String name) {
        Optional<Alias> alias = this.aliasRepository.findByGameAndAliasName(game, name);

        if (alias.isEmpty()) {
            throw new AliasNotFoundException(game, name);
        }

        return alias.get();
    }

}
