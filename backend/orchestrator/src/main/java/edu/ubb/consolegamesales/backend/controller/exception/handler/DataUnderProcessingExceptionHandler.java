package edu.ubb.consolegamesales.backend.controller.exception.handler;

import edu.ubb.consolegamesales.backend.service.exception.DataUnderProcessingException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataUnderProcessingExceptionHandler {
    @ExceptionHandler(DataUnderProcessingException.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void handleFailedConstraintException() {
    }
}
