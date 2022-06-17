package com.utopiapp.demo.controllers;

import com.utopiapp.demo.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    Map<String, String> errors = new HashMap<>();

    @ExceptionHandler({
            IncorrectPasswordException.class,
            EmptyFieldsException.class,
            UnauthorizedException.class,
            RareCharacterException.class,
            CodeNotExistsException.class
    })
    public ResponseEntity<?> exceptionHandler(RuntimeException runtimeException){
        errors.clear();
        if (runtimeException instanceof IncorrectPasswordException){
            errors.put("message", new IncorrectPasswordException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        } else if (runtimeException instanceof EmptyFieldsException){
            errors.put("message", new EmptyFieldsException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }else if (runtimeException instanceof UnauthorizedException){
            errors.put("message", new UnauthorizedException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        }else if (runtimeException instanceof RareCharacterException){
            errors.put("message", new RareCharacterException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }else if (runtimeException instanceof CodeNotExistsException){
            errors.put("message", new CodeNotExistsException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AlreadyInAClubException.class,
            AtLeastOneCoordinatorException.class,
            ClubNameInUseException.class,
            ClubEmailInUseException.class
    })
    public ResponseEntity<?> clubExceptions(RuntimeException runtimeException){
        errors.clear();
        if (runtimeException instanceof AlreadyInAClubException){
            errors.put("message", new AlreadyInAClubException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
        } else if (runtimeException instanceof AtLeastOneCoordinatorException){
            errors.put("message", new AtLeastOneCoordinatorException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
        } else if (runtimeException instanceof ClubNameInUseException){
            errors.put("message", new ClubNameInUseException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
        } else if (runtimeException instanceof ClubEmailInUseException){
            errors.put("message", new ClubEmailInUseException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PetitionTimeNotExpired.class, PetitionWasRejectedException.class})
    public ResponseEntity<?> petitionsException(RuntimeException runtimeException){
        errors.clear();
        if (runtimeException instanceof PetitionTimeNotExpired){
            errors.put("message", new PetitionTimeNotExpired().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.TOO_EARLY);
        } else if (runtimeException instanceof PetitionWasRejectedException){
            errors.put("message", new PetitionWasRejectedException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

