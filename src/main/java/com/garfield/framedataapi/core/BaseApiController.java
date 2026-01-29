package com.garfield.framedataapi.core;

import com.garfield.framedataapi.advice.authorization.Authenticated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.net.URI;
import java.util.UUID;

@Authenticated
@CrossOrigin
public abstract class BaseApiController {

    @Value("${server.servlet.context-path}")
    private String basePath;

    protected abstract String getRequestMapping();

    public URI createControllerUri(String path) {
        return URI.create(String.format("%s/%s/%s", basePath, getRequestMapping(), path));
    }

    public URI createControllerUri(UUID path) {
        return createControllerUri(String.valueOf(path));
    }

}
