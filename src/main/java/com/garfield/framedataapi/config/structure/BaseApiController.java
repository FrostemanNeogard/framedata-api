package com.garfield.framedataapi.config.structure;

import com.garfield.framedataapi.config.authorization.Authenticated;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;

@Authenticated
public abstract class BaseApiController {

    @Value("${server.servlet.context-path}")
    private String basePath;

    protected abstract String getRequestMapping();

    protected URI createControllerUri(String path) {
        return URI.create(String.format("%s/%s/%s", basePath, getRequestMapping(), path));
    }

}
