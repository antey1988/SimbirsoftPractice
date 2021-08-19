package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.feign.NotAvailablePaymentServiceException;
import com.example.SimbirsoftPractice.feign.PaymentClient;
import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;
import com.example.SimbirsoftPractice.services.PaymentService;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PaymentClient paymentClient;
    private final MessageSource messageSource;

    public PaymentServiceImpl(PaymentClient paymentClient, MessageSource messageSource) {
        this.paymentClient = paymentClient;
        this.messageSource = messageSource;
    }

    @Override
    public void createClient(CustomerWithUUIDRequestDto customer, Locale locale) {
        try {
            ResponseEntity<String> response = paymentClient.createClient(customer);
            logger.info(response.getBody());
        } catch (RetryableException e) {
            writeLog(e, locale);
        }
    }

    @Override
    public void payProject(PaymentProjectRequestDto project, Locale locale) {
        try {
            ResponseEntity<String> response = paymentClient.payProject(project);
            logger.info(response.getBody());
        } catch (RetryableException e) {
            writeLog(e, locale);
        }
    }

    private void writeLog(RetryableException e, Locale locale) {
        String text = messageSource.getMessage("error.PaymentServiceNotAvailable", null,  locale);
        logger.error(text);
        throw new NotAvailablePaymentServiceException(text);
    }
}
