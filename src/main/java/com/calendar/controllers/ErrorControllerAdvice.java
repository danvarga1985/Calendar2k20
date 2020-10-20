package com.calendar.controllers;

import com.calendar.exceptions.*;
import com.calendar.responsedto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<ApiError> handleUserNotLoggedIn(UserNotLoggedInException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(), ex.getMessage()));
    }

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<ApiError> handleEntryNotFound(EntryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage()));
    }

    @ExceptionHandler(SQLError.class)
    public ResponseEntity<ApiError> handleGenericSqlError(SQLError ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiError(
                HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()));
    }

    @ExceptionHandler(InvalidEntryTypeException.class)
    public ResponseEntity<ApiError> handleInvalidEntryType(InvalidEntryTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()));
    }
}
