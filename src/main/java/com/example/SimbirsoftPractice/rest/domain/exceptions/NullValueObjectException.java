package com.example.SimbirsoftPractice.rest.domain.exceptions;

public class NullValueObjectException extends RuntimeException {
    public NullValueObjectException() {
    }

    public NullValueObjectException(String message) {
        super(message);
    }
}
