package com.garfield.framedataapi.gameMetadata;

import com.garfield.framedataapi.games.Game;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "game_metadata")
public class GameMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(columnDefinition = "jsonb")
    private String attributesTemplate;

}
