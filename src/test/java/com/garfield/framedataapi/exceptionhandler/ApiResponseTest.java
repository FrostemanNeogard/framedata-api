package com.garfield.framedataapi.exceptionhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garfield.framedataapi.games.Game;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void success_shouldSetStatusAndDataStringWithoutError() {
        String data = "test-data";
        ApiResponse<String> response = ApiResponse.success(HttpStatus.OK, data);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(data, response.getData());
        assertNull(response.getError());
    }

    @Test
    void success_shouldSetStatusAndDataObjectWithoutError() {
        Game data = new Game("tekken");
        ApiResponse<Game> response = ApiResponse.success(HttpStatus.OK, data);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(data, response.getData());
        assertNull(response.getError());
    }

    @Test
    void error_shouldSetStatusAndErrorWithoutData() {
        String errorMessage = "Something went wrong";
        ApiResponse<Void> response = ApiResponse.error(HttpStatus.BAD_REQUEST, errorMessage);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(errorMessage, response.getError());
        assertNull(response.getData());
    }

    @Test
    void created_shouldSetCreatedStatusWithoutErrorOrData() {
        ApiResponse<Void> response = ApiResponse.created();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNull(response.getError());
        assertNull(response.getData());
    }

    @Test
    void jsonSerialization_shouldExcludeNullFields() throws JsonProcessingException {
        ApiResponse<Void> response = ApiResponse.created();

        String json = objectMapper.writeValueAsString(response);

        assertTrue(json.contains("\"status\":201"));
        assertFalse(json.contains("error"));
        assertFalse(json.contains("data"));
    }

}
