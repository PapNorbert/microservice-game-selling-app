package edu.ubb.consolegamesales.backend.controller.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class InvalidCreationExceptionHandler {
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errors = exception.getBindingResult().getFieldErrors().stream().map(fieldError ->
                fieldError.getField() + " " + fieldError.getDefaultMessage()).collect(Collectors.joining(", "));
        LOGGER.warn("Invalid entity at creation " + errors);
        return errors;
    }
}

