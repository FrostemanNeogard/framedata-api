package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.gameCharacters.GameCharacter;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AliasesService {

    private final AliasesRepository aliasesRepository;

    public AliasesService(AliasesRepository aliasesRepository) {
        this.aliasesRepository = aliasesRepository;
    }

    public Set<Alias> getAliasesForGameCharacter(GameCharacter gameCharacter) {
        return this.aliasesRepository.findAllByGameCharacter(gameCharacter);
    }

    public void createAlias(Alias alias) {
        this.aliasesRepository.save(alias);
    }
}
