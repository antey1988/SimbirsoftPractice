package com.example.SimbirsoftPractice.feign;

public class NotAvailablePaymentServiceException extends RuntimeException {
    public NotAvailablePaymentServiceException() {
    }

    public NotAvailablePaymentServiceException(String message) {
        super(message);
    }
}
