package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.feign.NotAvailablePaymentServiceException;
import com.example.SimbirsoftPractice.feign.PaymentClient;
import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;
import feign.RetryableException;
import org.aspectj.bridge.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private PaymentClient paymentClient;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Test
    void createClient() {
        CustomerWithUUIDRequestDto request = new CustomerWithUUIDRequestDto();
        Mockito.when(paymentClient.createClient(Mockito.any())).thenReturn(ResponseEntity.ok().build());
        paymentService.createClient(request, null);
        Mockito.verify(paymentClient).createClient(request);
    }

    @Test
    void payProject() {
        PaymentProjectRequestDto request = new PaymentProjectRequestDto();
        Mockito.when(paymentClient.payProject(Mockito.any())).thenReturn(ResponseEntity.ok().build());
        paymentService.payProject(request, null);
        Mockito.verify(paymentClient).payProject(request);
    }

    @Test
    void pingService() {
        Mockito.when(paymentClient.createClient(null)).thenThrow(RetryableException.class);
        assertThrows(NotAvailablePaymentServiceException.class,
                () -> paymentService.createClient(null, null));
    }
}