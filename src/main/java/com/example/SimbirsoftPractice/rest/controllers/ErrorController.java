package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.feign.NotAvailablePaymentServiceException;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotEnoughMoneyException;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalDateException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalStatusException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueObjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<String> handleNotEnoughMoneyException(NotEnoughMoneyException e) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(e.getMessage());
    }

    @ExceptionHandler({IllegalStatusException.class, NullValueFieldException.class,
            NullValueObjectException.class, IllegalDateException.class})
    public ResponseEntity<String> handleIllegalStateStatusException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotAvailablePaymentServiceException.class)
    public ResponseEntity<String> handleNotСreatedClientException(NotAvailablePaymentServiceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
