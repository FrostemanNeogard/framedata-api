package com.garfield.framedataapi.framedata;

import org.springframework.stereotype.Service;

@Service
public class FramedataService {

    private final FramedataRepository framedataRepository;

    public FramedataService(FramedataRepository framedataRepository) {
        this.framedataRepository = framedataRepository;
    }

}
