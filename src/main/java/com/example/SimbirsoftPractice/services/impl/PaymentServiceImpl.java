package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.feign.NotAvailablePaymentServiceException;
import com.example.SimbirsoftPractice.feign.PaymentClient;
import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;
import com.example.SimbirsoftPractice.services.PaymentService;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PaymentClient paymentClient;

    public PaymentServiceImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @Override
    public void createClient(CustomerWithUUIDRequestDto customer) {
        try {
            ResponseEntity<String> response = paymentClient.createClient(customer);
            logger.info(response.getBody());
        } catch (RetryableException e) {
            String text = "Платежный сервис не доступен";
            logger.warn(e.getMessage());
            logger.warn(text);
            throw new NotAvailablePaymentServiceException(text);
        }
    }

    @Override
    public void payProject(PaymentProjectRequestDto project) {
        try {
            ResponseEntity<String> response = paymentClient.payProject(project);
            logger.info(response.getBody());
        } catch (RetryableException e) {
            String text = "Платежный сервис не доступен";
            logger.warn(e.getMessage());
            logger.warn(text);
            throw new NotAvailablePaymentServiceException(text);
        }
    }
}
