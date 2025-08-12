package com.garfield.framedataapi.exceptionhandler;

import com.garfield.framedataapi.games.exceptions.GameAlreadyExistsException;
import com.garfield.framedataapi.games.exceptions.GameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({GameNotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handleGameNotFoundException(GameNotFoundException e) {
        return ApiResponseEntity.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({GameAlreadyExistsException.class})
    public ResponseEntity<ApiResponse<String>> handleGameAlreadyExistsException(GameAlreadyExistsException e) {
        return ApiResponseEntity.error(HttpStatus.CONFLICT, e.getMessage());
    }

}
