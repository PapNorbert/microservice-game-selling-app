package edu.ubb.consolegamesales.backend.controller.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class HttpMessageNotReadableExceptionHandler {

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleHttpMessageNotReadableException(InvalidFormatException exception) {
        if (exception.getTargetType().isEnum()) {
            String message = "Invalid value '" + exception.getValue().toString() +
                    "', possible option: " + Arrays.toString(exception.getTargetType().getEnumConstants());
            LOGGER.warn(message);
            return message;
        }
        LOGGER.warn("Invalid request body");
        return "Invalid request body";
    }

}
