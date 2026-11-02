
package com.learning.customer_service.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {

                    String field = fieldError.getField();
                    String message = fieldError.getDefaultMessage();

                    // Give NotBlank errors priority
                    if ("NotBlank".equals(fieldError.getCode())) {
                        errors.put(field, message);
                    } else {
                        errors.putIfAbsent(field, message);
                    }
                });

        ApiError response = new ApiError(
                Instant.now(),
                400,
                "Bad Request",
                "Request validation failed",
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleStatusException(
            ResponseStatusException ex) {

        HttpStatus status = HttpStatus.resolve(
                ex.getStatusCode().value()
        );

        String error = status != null
                ? status.getReasonPhrase()
                : "HTTP Error";

        ApiError response = new ApiError(
                Instant.now(),
                ex.getStatusCode().value(),
                error,
                ex.getReason(),
                Map.of()
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidJson(
            HttpMessageNotReadableException ex) {

        ApiError response = new ApiError(
                Instant.now(),
                400,
                "Bad Request",
                "Malformed or missing JSON request body",
                Map.of()
        );

        return ResponseEntity.badRequest().body(response);
    }
}
