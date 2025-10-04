package com.garfield.framedataapi.gameMetadata;

import org.springframework.stereotype.Service;

@Service
public class GameMetadatasService {

    private final GameMetadatasRepository gameMetadatasRepository;

    public GameMetadatasService(GameMetadatasRepository gameMetadatasRepository) {
        this.gameMetadatasRepository = gameMetadatasRepository;
    }

}
