package com.garfield.framedataapi.framedata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FramedataService {

    private final FramedataRepository framedataRepository;

}
