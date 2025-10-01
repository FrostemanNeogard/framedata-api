package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.gameCharacters.GameCharacter;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "framedata")
@Getter
public class Framedata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private GameCharacter gameCharacter;

    @Column(columnDefinition = "jsonb")
    private String attributes;

}
