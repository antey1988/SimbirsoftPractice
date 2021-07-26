package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto getCustomer(Long id);
    CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto);
    CustomerResponseDto updateCustomer(CustomerRequestDto customerRequestDto, Long id);
    void deleteCustomer(Long id);
    List<CustomerResponseDto> getListCustomers();
}
