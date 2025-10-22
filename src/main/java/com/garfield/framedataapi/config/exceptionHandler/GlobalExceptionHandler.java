package com.garfield.framedataapi.config.exceptionHandler;

import com.garfield.framedataapi.aliases.exceptions.AliasAlreadyExistsException;
import com.garfield.framedataapi.aliases.exceptions.AliasNotFoundException;
import com.garfield.framedataapi.config.structure.ApiResponse;
import com.garfield.framedataapi.config.structure.ApiResponseEntity;
import com.garfield.framedataapi.framedata.exceptions.*;
import com.garfield.framedataapi.gameCharacters.exceptions.AmbiguousGameCharacterNameException;
import com.garfield.framedataapi.gameCharacters.exceptions.GameCharacterAlreadyExistsException;
import com.garfield.framedataapi.gameCharacters.exceptions.GameCharacterNotFoundException;
import com.garfield.framedataapi.games.exceptions.GameAlreadyExistsException;
import com.garfield.framedataapi.games.exceptions.GameNotFoundException;
import com.garfield.framedataapi.games.exceptions.InvalidAttributesTemplateJson;
import com.garfield.framedataapi.users.exceptions.UserBannedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        if (e.getRequiredType() != null && e.getRequiredType().equals(UUID.class)) {
            return ApiResponseEntity.error(HttpStatus.BAD_REQUEST, String.format("\"%s\" is not a valid UUID.", e.getValue()));
        }

        return ApiResponseEntity.error(HttpStatus.BAD_REQUEST, String.format("\"%s\" is not a valid \"%s\".", e.getValue(), e.getRequiredType()));
    }

    @ExceptionHandler({AliasAlreadyExistsException.class})
    public ResponseEntity<ApiResponse<String>> handleAliasAlreadyExistsException(AliasAlreadyExistsException e) {
        return ApiResponseEntity.error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({AliasNotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handleAliasNotFoundException(AliasNotFoundException e) {
        return ApiResponseEntity.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({FramedataAlreadyExistsException.class})
    public ResponseEntity<ApiResponse<String>> handleFramedataAlreadyExistsException(FramedataAlreadyExistsException e) {
        return ApiResponseEntity.error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({FramedataJsonInvalidFieldTypeException.class})
    public ResponseEntity<ApiResponse<String>> handleFramedataJsonInvalidFieldTypeException(FramedataJsonInvalidFieldTypeException e) {
        return ApiResponseEntity.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({FramedataJsonMissingRequiredFieldException.class})
    public ResponseEntity<ApiResponse<String>> handleFramedataJsonMissingRequiredFieldException(FramedataJsonMissingRequiredFieldException e) {
        return ApiResponseEntity.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({FramedataNotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handleFramedataNotFoundException(FramedataNotFoundException e) {
        return ApiResponseEntity.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({JsonFormatException.class})
    public ResponseEntity<ApiResponse<String>> handleInvalidFramedataJsonException(JsonFormatException e) {
        return ApiResponseEntity.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({UnknownInternalErrorException.class})
    public ResponseEntity<ApiResponse<String>> handleUnknownInternalErrorException(UnknownInternalErrorException e) {
        return ApiResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler({AmbiguousGameCharacterNameException.class})
    public ResponseEntity<ApiResponse<String>> handleAmbiguousGameCharacterNameException(AmbiguousGameCharacterNameException e) {
        return ApiResponseEntity.error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({GameCharacterAlreadyExistsException.class})
    public ResponseEntity<ApiResponse<String>> handleGameCharacterAlreadyExistsException(GameCharacterAlreadyExistsException e) {
        return ApiResponseEntity.error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({GameCharacterNotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handleGameCharacterNotFoundException(GameCharacterNotFoundException e) {
        return ApiResponseEntity.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({GameAlreadyExistsException.class})
    public ResponseEntity<ApiResponse<String>> handleGameAlreadyExistsException(GameAlreadyExistsException e) {
        return ApiResponseEntity.error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({GameNotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handleGameNotFoundException(GameNotFoundException e) {
        return ApiResponseEntity.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({InvalidAttributesTemplateJson.class})
    public ResponseEntity<ApiResponse<String>> handleInvalidAttributesTemplateJson(InvalidAttributesTemplateJson e) {
        return ApiResponseEntity.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({UserBannedException.class})
    public ResponseEntity<ApiResponse<String>> handleUserBannedException(UserBannedException e) {
        return ApiResponseEntity.error(HttpStatus.FORBIDDEN, e.getMessage());
    }

}
