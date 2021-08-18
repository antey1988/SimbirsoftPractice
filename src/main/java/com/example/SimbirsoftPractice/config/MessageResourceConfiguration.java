package com.example.SimbirsoftPractice.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageResourceConfiguration {

    @Bean
    public MessageSource messageSource() {
         ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
         messageSource.setBasename("messages");
         messageSource.setDefaultEncoding("UTF-8");
         messageSource.setUseCodeAsDefaultMessage(true);
         return messageSource;
    }
}
