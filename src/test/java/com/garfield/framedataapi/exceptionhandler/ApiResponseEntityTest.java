package com.garfield.framedataapi.exceptionhandler;

import com.garfield.framedataapi.config.structure.ApiResponse;
import com.garfield.framedataapi.config.structure.ApiResponseEntity;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseEntityTest {

    @Test
    void ok_shouldReturn200WithData() {
        String data = "hello";
        ResponseEntity<ApiResponse<String>> response = ApiResponseEntity.ok(data);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
        assertEquals(data, response.getBody().getData());
        assertNull(response.getBody().getError());
    }

    @Test
    void created_withData_shouldReturn201WithLocationAndData() {
        URI location = URI.create("/resource/1");
        String data = "created";
        ResponseEntity<ApiResponse<String>> response = ApiResponseEntity.created(location, data);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(location, response.getHeaders().getLocation());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED.value(), response.getBody().getStatus());
        assertEquals(data, response.getBody().getData());
        assertNull(response.getBody().getError());
    }

    @Test
    void created_withoutData_shouldReturn201WithLocationAndNoData() {
        URI location = URI.create("/resource/2");
        ResponseEntity<ApiResponse<Void>> response = ApiResponseEntity.created(location);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(location, response.getHeaders().getLocation());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED.value(), response.getBody().getStatus());
        assertNull(response.getBody().getData());
        assertNull(response.getBody().getError());
    }

    @Test
    void status_shouldReturnCustomStatusWithData() {
        String data = "partial";
        ResponseEntity<ApiResponse<String>> response = ApiResponseEntity.status(HttpStatus.PARTIAL_CONTENT, data);

        assertEquals(HttpStatus.PARTIAL_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.PARTIAL_CONTENT.value(), response.getBody().getStatus());
        assertEquals(data, response.getBody().getData());
        assertNull(response.getBody().getError());
    }

    @Test
    void error_shouldReturnCustomStatusWithErrorMessage() {
        String errorMessage = "Bad request occurred";
        ResponseEntity<ApiResponse<Void>> response = ApiResponseEntity.error(HttpStatus.BAD_REQUEST, errorMessage);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getError());
        assertNull(response.getBody().getData());
    }

}
