package com.garfield.framedataapi.framedata;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "framedata")
public class Framedata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

}
