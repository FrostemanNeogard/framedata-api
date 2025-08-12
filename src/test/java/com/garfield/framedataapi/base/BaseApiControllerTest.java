package com.garfield.framedataapi.base;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = BaseApiControllerTest.TestConfig.class)
class BaseApiControllerTest {

    @Autowired
    private TestApiController testApiController;

    @Test
    void baseApiController_shouldReturnProperBasePath() {
        URI response = testApiController.createControllerUri("path");
        URI expected = URI.create("/api/v1/test/path");

        assertEquals(expected, response);
    }

    @Configuration
    static class TestConfig {
        @Bean
        TestApiController testApiController() {
            return new TestApiController();
        }
    }

    static class TestApiController extends BaseApiController {
        @Override
        protected String getRequestMapping() {
            return "test";
        }
    }
}