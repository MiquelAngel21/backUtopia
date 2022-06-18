package com.utopiapp.demo.controllers;

import com.utopiapp.demo.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.InvocationTargetException;
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
            CodeNotExistsException.class,
            InternalAuthenticationServiceException.class,
            IllegalArgumentException.class,
            EmailAlreadyExistsException.class,
            UsernameAlreadyExistsException.class,
            TagNoExists.class,
            OAuth2GitHubAuthenticationException.class,
            EmptyPasswordException.class,
            NegativeAmountException.class,
            InvalidImageException.class
    })
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
            System.out.println("HEY HEY HEY");
            errors.put("message", new RareCharacterException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        }else if (runtimeException instanceof CodeNotExistsException){
            errors.put("message", new CodeNotExistsException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        }else if (runtimeException instanceof InternalAuthenticationServiceException){
            errors.put("message", "La contrasenya o el email son incorrectes");
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        }else if (runtimeException instanceof IllegalArgumentException){
            errors.put("message", "Tots els camps són necessaris!");
            return new ResponseEntity<>(errors, HttpStatus.NO_CONTENT);
        } else if (runtimeException instanceof UsernameAlreadyExistsException){
            errors.put("message", new UsernameAlreadyExistsException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        } else if (runtimeException instanceof EmailAlreadyExistsException){
            errors.put("message", new EmailAlreadyExistsException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
        } else if (runtimeException instanceof TagNoExists){
            errors.put("message", new TagNoExists().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
        } else if (runtimeException instanceof OAuth2GitHubAuthenticationException){
            errors.put("message", new OAuth2GitHubAuthenticationException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
        } else if (runtimeException instanceof EmptyPasswordException){
            errors.put("message", new EmptyPasswordException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        } else if (runtimeException instanceof NegativeAmountException){
            errors.put("message", new NegativeAmountException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        } else if (runtimeException instanceof InvalidImageException){
            errors.put("message", new InvalidImageException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AlreadyInAClubException.class, AtLeastOneCoordinatorException.class})
    public ResponseEntity<?> clubExceptions(RuntimeException runtimeException){
        errors.clear();
        if (runtimeException instanceof AlreadyInAClubException){
            errors.put("message", new AlreadyInAClubException().getMessage());
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
        } else if (runtimeException instanceof AtLeastOneCoordinatorException){
            errors.put("message", new AtLeastOneCoordinatorException().getMessage());
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

