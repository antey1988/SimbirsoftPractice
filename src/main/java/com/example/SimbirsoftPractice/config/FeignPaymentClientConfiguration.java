package com.example.SimbirsoftPractice.config;

import com.example.SimbirsoftPractice.feign.PaymentClientDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignPaymentClientConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new PaymentClientDecoder();
    }
}
