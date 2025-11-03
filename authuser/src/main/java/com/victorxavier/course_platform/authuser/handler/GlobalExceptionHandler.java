package com.victorxavier.course_platform.authuser.handler;

import com.victorxavier.course_platform.authuser.exception.DataConflictException;
import com.victorxavier.course_platform.authuser.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        log.warn("Resource not found: {}", e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorResponse> dataConflict(DataConflictException e, HttpServletRequest request) {
        String error = "Business Rule Conflict";
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        log.warn("Business rule conflict: {}", e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        String error = "Invalid argument";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        log.warn("Invalid argument: {}", e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e, HttpServletRequest request) {
        String error = "Invalid state";
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        log.warn("Invalid state: {}", e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<ErrorResponse.FieldMessage> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorResponse.FieldMessage(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        String message = String.format("Validation failed for object '%s'. Error count: %d.",
                ex.getBindingResult().getObjectName(), ex.getBindingResult().getErrorCount());

        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Validation error",
                message,
                request.getContextPath(),
                validationErrors
        );
        log.warn("Validation error: {}", validationErrors);
        return ResponseEntity.status(status).body(err);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        String error = "Invalid JSON format";
        String message = "The request body is malformed. Please check the JSON format.";
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error, message, request.getContextPath());
        log.warn("Malformed JSON: {}", ex.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String error = "Invalid parameter type";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String paramName = e.getName();
        String requiredType = Objects.requireNonNull(e.getRequiredType()).getSimpleName();
        String message = String.format("Parameter '%s' failed conversion. Expected: %s.", paramName, requiredType);

        if (requiredType.equals("UUID")) {
            message = String.format("The ID '%s' provided in the path is not a valid UUID.", e.getValue());
        }

        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error, message, request.getRequestURI());
        log.warn("Argument type mismatch: {}", message);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handlePropertyReference(PropertyReferenceException e, HttpServletRequest request) {
        String error = "Invalid sort parameter";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = String.format("The sort field '%s' does not exist.", e.getPropertyName());

        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error, message, request.getRequestURI());
        log.warn("Invalid sort parameter: {}", message);
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException e, HttpServletRequest request) {
        String error = "Data integrity violation";
        HttpStatus status = HttpStatus.CONFLICT;
        String message = "Data integrity violation (e.g., duplicate record).";

        String rootCause = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();
        if (rootCause != null) {
            if (rootCause.toLowerCase().contains("username")) {
                message = "Username already exists";
            } else if (rootCause.toLowerCase().contains("email")) {
                message = "Email is already in use";
            }
        }

        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error, message, request.getRequestURI());
        log.warn("Data integrity violation: {}", rootCause);
        return ResponseEntity.status(status).body(err);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e, HttpServletRequest request) {
        String error = "Internal server error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "An unexpected error occurred on the server.";
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error, message, request.getRequestURI());
        log.error("Unhandled internal error [{}]: {}", request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.status(status).body(err);
    }

}