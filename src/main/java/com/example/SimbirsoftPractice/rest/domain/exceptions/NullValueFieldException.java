package com.example.SimbirsoftPractice.rest.domain.exceptions;

public class NullValueFieldException extends RuntimeException {
    public NullValueFieldException() {
    }

    public NullValueFieldException(String message) {
        super(message);
    }
}
