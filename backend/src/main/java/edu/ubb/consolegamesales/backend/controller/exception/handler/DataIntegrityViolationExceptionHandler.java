package edu.ubb.consolegamesales.backend.controller.exception.handler;

import edu.ubb.consolegamesales.backend.model.ErrorData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class DataIntegrityViolationExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorData handleFailedConstraintException(DataIntegrityViolationException exception) {
        LOGGER.error("Error creating GameDisc, " + exception.getMessage());
        return new ErrorData("Path variable type not supported");
    }
}
