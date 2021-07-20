package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.controllers.exceptions.TestRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler({IOException.class, TestRuntimeException.class})
    public ResponseEntity handlerException(Exception e) {
        return new ResponseEntity(e.toString(), HttpStatus.BAD_REQUEST);
    }
}