package com.garfield.framedataapi.framedata;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface FramedataRepository extends ListCrudRepository<Framedata, UUID> {
}
