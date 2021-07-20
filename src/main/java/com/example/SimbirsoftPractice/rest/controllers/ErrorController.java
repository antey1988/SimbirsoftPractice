package com.example.SimbirsoftPractice.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(IOException.class)
    public ResponseEntity handlerException(IOException e) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
