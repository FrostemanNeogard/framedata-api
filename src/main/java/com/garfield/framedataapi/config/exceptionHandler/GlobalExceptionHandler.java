package com.garfield.framedataapi.config.exceptionHandler;

import com.garfield.framedataapi.aliases.exceptions.AmbiguousCharacterNameException;
import com.garfield.framedataapi.config.structure.ApiResponse;
import com.garfield.framedataapi.config.structure.ApiResponseEntity;
import com.garfield.framedataapi.gameCharacters.exceptions.GameCharacterNotFoundException;
import com.garfield.framedataapi.games.exceptions.GameAlreadyExistsException;
import com.garfield.framedataapi.games.exceptions.GameNotFoundException;
import com.garfield.framedataapi.users.exceptions.UserBannedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AmbiguousCharacterNameException.class})
    public ResponseEntity<ApiResponse<String>> handleAmbiguousCharacterNameException(AmbiguousCharacterNameException e) {
        return ApiResponseEntity.error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({GameCharacterNotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handleGameCharacterNotFoundException(GameCharacterNotFoundException e) {
        return ApiResponseEntity.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({GameNotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handleGameNotFoundException(GameNotFoundException e) {
        return ApiResponseEntity.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({GameAlreadyExistsException.class})
    public ResponseEntity<ApiResponse<String>> handleGameAlreadyExistsException(GameAlreadyExistsException e) {
        return ApiResponseEntity.error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({UserBannedException.class})
    public ResponseEntity<ApiResponse<String>> handleUserBannedException(UserBannedException e) {
        return ApiResponseEntity.error(HttpStatus.FORBIDDEN, e.getMessage());
    }

}
