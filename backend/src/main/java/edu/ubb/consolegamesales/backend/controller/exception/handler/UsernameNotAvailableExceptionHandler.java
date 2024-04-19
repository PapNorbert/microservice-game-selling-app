package edu.ubb.consolegamesales.backend.controller.exception.handler;

import edu.ubb.consolegamesales.backend.model.ErrorData;
import edu.ubb.consolegamesales.backend.service.exception.UsernameNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class UsernameNotAvailableExceptionHandler {
    @ExceptionHandler(UsernameNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorData handleFailedConstraintException(UsernameNotAvailableException exception) {
        LOGGER.error(exception.getMessage());
        return new ErrorData(exception.getMessage());
    }
}
