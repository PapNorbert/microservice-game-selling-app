package edu.ubb.consolegamesales.backend.service.exception;

public class AnnouncementAlreadySoldException extends RuntimeException {
    public AnnouncementAlreadySoldException() {
    }

    public AnnouncementAlreadySoldException(String message) {
        super(message);
    }

    public AnnouncementAlreadySoldException(String message, Throwable cause) {
        super(message, cause);
    }
}
