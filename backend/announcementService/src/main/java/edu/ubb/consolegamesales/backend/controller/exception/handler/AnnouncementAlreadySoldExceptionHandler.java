package edu.ubb.consolegamesales.backend.controller.exception.handler;

import edu.ubb.consolegamesales.backend.model.ErrorData;
import edu.ubb.consolegamesales.backend.service.exception.AnnouncementAlreadySoldException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AnnouncementAlreadySoldExceptionHandler {

    @ExceptionHandler(AnnouncementAlreadySoldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorData handleAnnouncementAlreadySoldException(AnnouncementAlreadySoldException ignoredException) {
        LOGGER.error("Error: trying to order an already sold announcement");
        return new ErrorData("Announcement already has been sold");
    }
}
