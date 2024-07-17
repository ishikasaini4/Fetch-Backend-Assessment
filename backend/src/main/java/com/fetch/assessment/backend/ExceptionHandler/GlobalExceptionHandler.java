package com.fetch.assessment.backend.ExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        errors.put("Error", HttpStatus.BAD_REQUEST.toString());

        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static ResponseEntity<Map<String, String>> handleCustomException(String error, String message) {
        Map<String, String> errors = new LinkedHashMap<>();

        errors.put("Error", HttpStatus.BAD_REQUEST.toString());
        errors.put(error, message);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static ResponseEntity<Map<String, String>> handleCustomException(Map<String, String>errorMap) {
        Map<String, String> errors = new LinkedHashMap<>();

        errors.put("Error", HttpStatus.BAD_REQUEST.toString());
        for(Map.Entry<String, String> e: errorMap.entrySet()){
            errors.put(e.getKey(),e.getValue());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

