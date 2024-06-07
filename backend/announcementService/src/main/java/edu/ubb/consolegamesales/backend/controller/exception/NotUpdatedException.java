package edu.ubb.consolegamesales.backend.controller.exception;

public class NotUpdatedException extends RuntimeException {
    public NotUpdatedException(String message) {
        super(message);
    }

    public NotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
