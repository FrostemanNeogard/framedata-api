package com.garfield.framedataapi.games;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends ListCrudRepository<Game, UUID> {
    Game getByNameIgnoreCase(String name);

    List<Game> findAll();

    Game getById(UUID id);
}

