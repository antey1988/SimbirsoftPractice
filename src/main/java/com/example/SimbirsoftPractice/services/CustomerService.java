package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;

import java.util.List;
import java.util.Locale;

public interface CustomerService {
    CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto, Locale locale);
    CustomerResponseDto readCustomer(Long id, Locale locale);
    CustomerResponseDto updateCustomer(CustomerRequestDto customerRequestDto, Long id, Locale locale);
    void deleteCustomer(Long id, Locale locale);
    List<CustomerResponseDto> getListCustomers();
}