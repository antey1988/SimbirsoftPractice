package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.mappers.CustomerMapper;
import com.example.SimbirsoftPractice.repos.CustomerRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.repository = customerRepository;
        this.mapper = customerMapper;
    }

    public CustomerResponseDto readCustomer(Long id) {
        CustomerEntity customerEntity = getOrElseThrow(id);
        return mapper.entityToResponseDto(customerEntity);
    }

    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
        CustomerEntity customerEntity = mapper.requestDtoToEntity(customerRequestDto, new CustomerEntity());
        customerEntity = repository.save(customerEntity);
        return mapper.entityToResponseDto(customerEntity);
    }

    public CustomerResponseDto updateCustomer(CustomerRequestDto customerRequestDto, Long id) {
        CustomerEntity customerEntity = getOrElseThrow(id);
        customerEntity = mapper.requestDtoToEntity(customerRequestDto, customerEntity);
        customerEntity = repository.save(customerEntity);
        return mapper.entityToResponseDto(customerEntity);
    }

    public void deleteCustomer(Long id) {
        CustomerEntity customerEntity = getOrElseThrow(id);
        repository.deleteById(id);
    }

    public List<CustomerResponseDto> getListCustomers() {
        List<CustomerEntity> list = repository.findAll();
        return mapper.listEntityToListResponseDto(list);
    }

    private CustomerEntity getOrElseThrow(Long id ) {
        Optional<CustomerEntity> optionalCustomer = repository.findById(id);
        return optionalCustomer.orElseThrow(()->new NotFoundException(String.format("Клиента с id = %d не существует", id)));
    }
}
