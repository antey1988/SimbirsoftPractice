package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.mappers.CustomerMapper;
import com.example.SimbirsoftPractice.repos.CustomerRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.services.CustomerService;
import com.example.SimbirsoftPractice.services.CustomerValidatorService;
import com.example.SimbirsoftPractice.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final CustomerValidatorService validator;
    private final PaymentService paymentService;
    private final MessageSource messageSource;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper,
                               CustomerValidatorService validator, PaymentService paymentService, MessageSource messageSource) {
        this.repository = customerRepository;
        this.mapper = customerMapper;
        this.validator = validator;
        this.paymentService = paymentService;
        this.messageSource = messageSource;
    }

    @Override
    public CustomerResponseDto readCustomer(Long id, Locale locale) {
        CustomerEntity customerEntity = getOrElseThrow(id, locale);
        return mapper.entityToResponseDto(customerEntity);
    }

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
        CustomerEntity customerEntity = validator.validateOnCreation(customerRequestDto, new CustomerEntity());
        customerEntity = repository.save(customerEntity);
        CustomerResponseDto response = mapper.entityToResponseDto(customerEntity);
        paymentService.createClient(mapper.entityToResponseDtoWithUUID(customerEntity));
        logger.info("New record added to DB");
        return response;
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(CustomerRequestDto customerRequestDto, Long id, Locale locale) {
        CustomerEntity customerEntity = getOrElseThrow(id, locale);
        customerEntity = validator.validateOnUpdate(customerRequestDto, customerEntity);
        logger.info("Record updated in the DB");
        return mapper.entityToResponseDto(customerEntity);
    }

    @Override
    public void deleteCustomer(Long id, Locale locale) {
        CustomerEntity customerEntity = getOrElseThrow(id, locale);
        repository.deleteById(id);
        logger.info("Record deleted from database");
    }

    @Override
    public List<CustomerResponseDto> getListCustomers() {
        List<CustomerEntity> list = repository.findAll();
        logger.info("All records retrieved from the DB");
        return mapper.listEntityToListResponseDto(list);
    }

    private CustomerEntity getOrElseThrow(Long id, Locale locale ) {
        logger.info(String.format("Extracting record with identifier(id) = %d from DB", id));
        Optional<CustomerEntity> optionalCustomer = repository.findById(id);
        return optionalCustomer.orElseThrow(() -> {
            logger.warn(String.format("Record with identifier (id) =% d does not exist", id));
            return new NotFoundException(String.format(messageSource.getMessage("error.NotFound", null, locale), id));
        });
    }
}