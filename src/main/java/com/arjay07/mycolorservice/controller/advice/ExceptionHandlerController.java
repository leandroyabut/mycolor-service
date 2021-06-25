package com.arjay07.mycolorservice.controller.advice;

import com.arjay07.mycolorservice.dto.ValidationErrorDTO;
import com.arjay07.mycolorservice.exception.BadRequestException;
import com.arjay07.mycolorservice.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j(topic = "Exception Handler")
public class ExceptionHandlerController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body(exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.TEXT_PLAIN)
                .body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handleValidationError(MethodArgumentNotValidException exception) {
        assert exception.getFieldError() != null;
        log.error("Bad request due to invalid request body: [\"{}\", \"{}\" ]", exception.getFieldError().getField(), exception.getFieldError().getDefaultMessage());
        ValidationErrorDTO validationError = ValidationErrorDTO.builder()
                .field(exception.getFieldError().getField())
                .message(exception.getFieldError().getDefaultMessage()).build();
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(validationError);
    }

}
