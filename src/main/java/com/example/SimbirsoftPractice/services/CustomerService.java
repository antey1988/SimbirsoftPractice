package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.mappers.CustomerMapper;
import com.example.SimbirsoftPractice.repos.CustomerRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerResponseDto getCustomer(Long id) {
        Optional<CustomerEntity> optionalCustomer = customerRepository.findById(id);
        CustomerEntity customerEntity = optionalCustomer.orElseThrow(()->new NotFoundException(String.format("Клиента с id = %d не существует", id)));
        return customerMapper.entityToResponseDto(customerEntity);
    }

    public CustomerResponseDto saveCustomer(CustomerRequestDto customerRequestDto) {
        CustomerEntity customerEntity = customerMapper.RequestDtoToEntity(customerRequestDto);
        customerEntity = customerRepository.save(customerEntity);
        return customerMapper.entityToResponseDto(customerEntity);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public List<CustomerResponseDto> listCustomers() {
        List<CustomerEntity> list = customerRepository.findAll();
        return customerMapper.listEntitiesToListResponseDto(list);
    }
}
