package com.higherx.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> exception(Exception e) {
        log.error("[Exception 발생] !!, message: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> runtimeException(RuntimeException e) {
        log.error("[RuntimeException 발생] !!, message: {}", e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> noSuchElementException(NoSuchElementException e) {
        log.error("[NoSuchElementException 발생] !!, message: {}", e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> unAuthorizedException(UnAuthorizedException e) {
        log.error("[UnAuthorizedException 발생] !!, message: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }
}
