package com.garfield.framedataapi.gameMetadata;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface GameMetadatasRepository extends ListCrudRepository<GameMetadata, UUID> {
}
