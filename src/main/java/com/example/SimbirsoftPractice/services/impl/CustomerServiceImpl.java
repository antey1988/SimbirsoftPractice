package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.mappers.CustomerMapper;
import com.example.SimbirsoftPractice.repos.CustomerRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.services.CustomerService;
import com.example.SimbirsoftPractice.services.validators.CustomerValidatorService;
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
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto, Locale locale) {
        CustomerEntity customerEntity = validator.validate(customerRequestDto, new CustomerEntity(), locale);
        customerEntity = repository.save(customerEntity);
        paymentService.createClient(mapper.entityToResponseDtoWithUUID(customerEntity), locale);
        logger.info("New Customer added to DB");
        return mapper.entityToResponseDto(customerEntity);
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(CustomerRequestDto customerRequestDto, Long id, Locale locale) {
        CustomerEntity customerEntity = getOrElseThrow(id, locale);
        customerEntity = validator.validate(customerRequestDto, customerEntity, locale);
        logger.info("Customer updated in the DB");
        return mapper.entityToResponseDto(customerEntity);
    }

    @Override
    public void deleteCustomer(Long id, Locale locale) {
        getOrElseThrow(id, locale);
        repository.deleteById(id);
        logger.info("Customer deleted from DB");
    }

    @Override
    public List<CustomerResponseDto> getListCustomers() {
        List<CustomerEntity> list = repository.findAll();
        logger.info("All Customers retrieved from the DB");
        return mapper.listEntityToListResponseDto(list);
    }

    private CustomerEntity getOrElseThrow(Long id, Locale locale ) {
        logger.info(String.format("Extracting Customer with identifier(id) = %d from DB", id));
        Optional<CustomerEntity> optional = repository.findById(id);
        return optional.orElseThrow(() -> {
            logger.warn(String.format("Customer with identifier (id) =% d does not exist", id));
            String error = messageSource.getMessage("error.NotFound", null, locale);
            String record = messageSource.getMessage("record.Customer", null, locale);
            return new NotFoundException(String.format(error, record, id));
        });
    }
}