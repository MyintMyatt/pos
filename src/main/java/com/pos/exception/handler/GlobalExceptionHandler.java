package com.pos.exception.handler;

import com.pos.common.model.response.ApiResponse;
import com.pos.exception.UserAlreadyExitedException;
import com.pos.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> userNotFoundException(NotFoundException e) {
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

    // 🔒 403 Forbidden – user is authenticated but not allowed
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> accessDenied(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .message("Access Denied or You have no permission")
                        .data(null)
                        .build());
    }

    // 🔐 401 Unauthorized – user is not authenticated
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> authenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message("Unauthorized")
                        .data(null)
                        .build());
    }
}
