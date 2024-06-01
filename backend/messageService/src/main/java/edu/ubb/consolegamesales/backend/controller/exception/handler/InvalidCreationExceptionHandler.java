package edu.ubb.consolegamesales.backend.controller.exception.handler;

import edu.ubb.consolegamesales.backend.model.ErrorData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InvalidCreationExceptionHandler {
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorData handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errors = exception.getBindingResult().getFieldErrors().stream().map(fieldError ->
                fieldError.getField() + " " + fieldError.getDefaultMessage()).collect(Collectors.joining(", "));
        LOGGER.error("Invalid entity at creation " + errors);
        return new ErrorData(errors);
    }
}

