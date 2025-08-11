package com.garfield.framedataapi.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public class ApiResponseEntity<T> {

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(URI location, T data) {
        return ResponseEntity.created(location)
                .body(ApiResponse.success(HttpStatus.CREATED, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(URI location) {
        return ResponseEntity.created(location).body(ApiResponse.created());
    }

    public static <T> ResponseEntity<ApiResponse<T>> status(HttpStatus status, T data) {
        return ResponseEntity.status(status).body(ApiResponse.success(status, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String errorMessage) {
        return ResponseEntity.status(status).body(ApiResponse.error(status, errorMessage));
    }
}
