package com.utopiapp.demo.controllers;

import com.utopiapp.demo.exceptions.AlreadyInAClubException;
import com.utopiapp.demo.exceptions.EmptyFieldsException;
import com.utopiapp.demo.exceptions.IncorrectPasswordException;
import com.utopiapp.demo.exceptions.RareCharacterException;
import com.utopiapp.demo.exceptions.UnauthorizedException;
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
        }else if (runtimeException instanceof UnauthorizedException){
            errors.put("message", new UnauthorizedException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        }else if (runtimeException instanceof RareCharacterException){
            errors.put("message", new RareCharacterException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AlreadyInAClubException.class})
    public ResponseEntity<?> clubExceptions(RuntimeException runtimeException){
        errors.clear();
        if (runtimeException instanceof AlreadyInAClubException){
            System.out.println("HEY");
            errors.put("message", new AlreadyInAClubException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

