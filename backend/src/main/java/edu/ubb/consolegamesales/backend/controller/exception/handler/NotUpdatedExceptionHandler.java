package edu.ubb.consolegamesales.backend.controller.exception.handler;

import edu.ubb.consolegamesales.backend.controller.exception.NotUpdatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NotUpdatedExceptionHandler {

    @ExceptionHandler(NotUpdatedException.class)
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public void handleFailedConstraintException(NotUpdatedException exception) {
        LOGGER.error(exception.getMessage());
    }
}
