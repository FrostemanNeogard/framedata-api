package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.aliases.exceptions.AliasAlreadyExistsException;
import com.garfield.framedataapi.aliases.exceptions.AliasNotFoundException;
import com.garfield.framedataapi.games.Game;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AliasService {

    private final AliasRepository aliasRepository;

    public void createAlias(Alias alias) {
        Optional<Alias> existingAlias = this.aliasRepository.findByGameAndAliasNameIgnoreCase(
                alias.getGame(),
                alias.getAliasName()
        );

        if (existingAlias.isPresent()) {
            throw new AliasAlreadyExistsException(existingAlias.get());
        }

        this.aliasRepository.save(alias);
    }

    @Transactional
    public Alias renameAlias(Alias alias, String newName) {
        String name = newName.trim();
        Optional<Alias> existingAlias = this.aliasRepository.findByGameAndAliasNameIgnoreCase(alias.getGame(), name);

        if (existingAlias.isPresent() && !existingAlias.get().getId().equals(alias.getId())) {
            throw new AliasAlreadyExistsException(existingAlias.get());
        }

        alias.rename(name);

        return this.aliasRepository.save(alias);
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
        Optional<Alias> alias = this.aliasRepository.findByGameAndAliasNameIgnoreCase(game, name);

        if (alias.isEmpty()) {
            throw new AliasNotFoundException(game, name);
        }

        return alias.get();
    }

    public void deleteAlias(Alias alias) {
        this.aliasRepository.delete(alias);
    }

}
