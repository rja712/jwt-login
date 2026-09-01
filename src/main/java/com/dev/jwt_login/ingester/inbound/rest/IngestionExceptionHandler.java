package com.dev.jwt_login.ingester.inbound.rest;

import com.dev.jwt_login.ingester.domain.exception.IngestionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Turns a rejected snapshot into 400 Bad Request.
 *
 * <p>Without this, an exception thrown while handling the request is forwarded to /error,
 * which requires authentication, so the caller sees a misleading 403 — as if the endpoint were
 * protected — instead of being told what was wrong with the payload.
 */
@RestControllerAdvice(assignableTypes = StockSnapshotIngestionController.class)
@Slf4j
public class IngestionExceptionHandler {

    /** Field-level checks on the request: missing, wrong shape, negative. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFields(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() == null ? "invalid" : error.getDefaultMessage(),
                        (first, second) -> first,
                        LinkedHashMap::new));

        log.warn("Rejected snapshot, invalid fields: {}", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Validation failed", "fields", fieldErrors));
    }

    /** Business rules that need several fields together, plus malformed JSON. */
    @ExceptionHandler({IngestionException.class, IllegalArgumentException.class,
            DateTimeParseException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, Object>> handleRejectedSnapshot(Exception exception) {
        log.warn("Rejected snapshot: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", exception.getMessage() == null ? "Invalid snapshot" : exception.getMessage()));
    }
}
