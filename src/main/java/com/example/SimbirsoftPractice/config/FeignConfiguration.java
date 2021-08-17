package com.example.SimbirsoftPractice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.example.SimbirsoftPractice.feign")
public class FeignConfiguration {
}
