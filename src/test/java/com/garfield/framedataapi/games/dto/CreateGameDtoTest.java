package com.garfield.framedataapi.games.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameDtoTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    void validName_shouldPassValidation() {
        CreateGameDto dto = new CreateGameDto("Tekken");
        Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankName_shouldFailValidation() {
        CreateGameDto dto = new CreateGameDto("");
        Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        ConstraintViolation<CreateGameDto> violation = violations.iterator().next();
        assertEquals("\"name\" field is required.", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void nullName_shouldFailValidation() {
        CreateGameDto dto = new CreateGameDto(null);
        Set<ConstraintViolation<CreateGameDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());

        ConstraintViolation<CreateGameDto> violation = violations.iterator().next();
        assertEquals("\"name\" field is required.", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }
}
