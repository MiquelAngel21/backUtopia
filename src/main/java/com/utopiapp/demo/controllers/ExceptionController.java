package com.utopiapp.demo.controllers;

import com.utopiapp.demo.exceptions.EmptyFieldsException;
import com.utopiapp.demo.exceptions.IncorrectPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    Map<String, String> errors = new HashMap<>();

    @ExceptionHandler({IncorrectPasswordException.class, EmptyFieldsException.class})
    public ResponseEntity<?> exceptionHandler(RuntimeException runtimeException){
        errors.clear();
        if (runtimeException instanceof IncorrectPasswordException){
            errors.put("message", new IncorrectPasswordException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        } else if (runtimeException instanceof EmptyFieldsException){
            errors.put("message", new EmptyFieldsException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

