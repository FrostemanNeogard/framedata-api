package com.garfield.framedataapi.base;

import org.springframework.beans.factory.annotation.Value;

import java.net.URI;

public abstract class BaseApiController {
    @Value("${server.servlet.context-path}")
    private String basePath;

    protected abstract String getRequestMapping();

    protected URI createControllerUri(String path) {
        return URI.create(String.format("%s/%s/%s", basePath, getRequestMapping(), path));
    }
}
