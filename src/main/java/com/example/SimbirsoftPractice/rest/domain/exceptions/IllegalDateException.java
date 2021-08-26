package com.example.SimbirsoftPractice.rest.domain.exceptions;

public class IllegalDateException extends RuntimeException {
    public IllegalDateException() {
    }

    public IllegalDateException(String message) {
        super(message);
    }
}
