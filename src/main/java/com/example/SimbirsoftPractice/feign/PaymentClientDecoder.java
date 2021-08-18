package com.example.SimbirsoftPractice.feign;

import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotEnoughMoneyException;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class PaymentClientDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        int status = response.status();
        String body = response.body().toString();
        switch (status) {
            case 402: {
                return new NotEnoughMoneyException(body);
            }
            case 404: {
                return new NotFoundException(body);
            }
        }
        return new RuntimeException();
    }
}
