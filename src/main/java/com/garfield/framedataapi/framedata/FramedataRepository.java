package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.gameCharacters.GameCharacter;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Set;
import java.util.UUID;

public interface FramedataRepository extends ListCrudRepository<Framedata, UUID> {
    Set<Framedata> findAllByGameCharacter(GameCharacter gameCharacter);
}
