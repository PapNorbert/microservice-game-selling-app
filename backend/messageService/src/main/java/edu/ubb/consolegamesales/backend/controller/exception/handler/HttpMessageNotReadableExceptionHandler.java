package edu.ubb.consolegamesales.backend.controller.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import edu.ubb.consolegamesales.backend.model.ErrorData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpMessageNotReadableExceptionHandler {

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorData handleInvalidFormatException(InvalidFormatException exception) {
        if (exception.getTargetType().isEnum()) {
            String message = "Invalid value '" + exception.getValue().toString()
                   + "', possible option: " + Arrays.toString(exception.getTargetType().getEnumConstants());
            LOGGER.error(message);
            return new ErrorData(message);
        }
        LOGGER.error("Invalid request body");
        return new ErrorData("Invalid request body");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorData handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        LOGGER.error("Invalid request body: " + exception.getMessage());
        return new ErrorData("Invalid request body");
    }
}
