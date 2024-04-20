package edu.ubb.consolegamesales.backend.service.exception;

public class UsernameNotAvailableException extends RuntimeException {

    public UsernameNotAvailableException(String message) {
        super(message);
    }

    public UsernameNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
