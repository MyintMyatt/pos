package com.pos.exception.handler;

import com.pos.common.ApiResponse;
import com.pos.exception.UserAlreadyExitedException;
import com.pos.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> userNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .data(null)
                        .build());

    }

    @ExceptionHandler(UserAlreadyExitedException.class)
    public ResponseEntity<ApiResponse<?>> userAlreadyExistedException(UserAlreadyExitedException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .data(null)
                        .build());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> globalExceptionHandler(Exception e) {
        return ResponseEntity.status(500)
                .body(
                        ApiResponse.<String>builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message("Internal server error")
                                .data(e.getMessage())
                                .build()
                );
    }
}
