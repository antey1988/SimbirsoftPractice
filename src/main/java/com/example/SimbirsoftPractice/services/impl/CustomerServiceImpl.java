package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.mappers.CustomerMapper;
import com.example.SimbirsoftPractice.repos.CustomerRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.services.CustomerService;
import com.example.SimbirsoftPractice.services.CustomerValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final CustomerValidatorService validator;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerMapper customerMapper, CustomerValidatorService validator) {
        this.repository = customerRepository;
        this.mapper = customerMapper;
        this.validator = validator;
    }
//    public CustomerServiceImpl(CustomerRepository customerRepository,
//                               CustomerMapper customerMapper) {
//        this.repository = customerRepository;
//        this.mapper = customerMapper;
//    }

    @Override
    public CustomerResponseDto readCustomer(Long id) {
        CustomerEntity customerEntity = getOrElseThrow(id);
        return mapper.entityToResponseDto(customerEntity);
    }

    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
//        CustomerEntity customerEntity = mapper.requestDtoToEntity(customerRequestDto, new CustomerEntity());
        CustomerEntity customerEntity = validator.validateOnCreation(customerRequestDto, new CustomerEntity());
        customerEntity = repository.save(customerEntity);
        logger.info("Новая запись добавлена в базу данных");
        return mapper.entityToResponseDto(customerEntity);
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(CustomerRequestDto customerRequestDto, Long id) {
        CustomerEntity customerEntity = getOrElseThrow(id);
        customerEntity = mapper.requestDtoToEntity(customerRequestDto, customerEntity);
        logger.info("Запись обновлена в базе данных");
        return mapper.entityToResponseDto(customerEntity);
    }

    @Override
    public void deleteCustomer(Long id) {
        CustomerEntity customerEntity = getOrElseThrow(id);
        repository.deleteById(id);
        logger.info("Запись удалена из базы данных");
    }

    @Override
    public List<CustomerResponseDto> getListCustomers() {
        List<CustomerEntity> list = repository.findAll();
        logger.info("Список записей извлечен из базы данных");
        return mapper.listEntityToListResponseDto(list);
    }

    private CustomerEntity getOrElseThrow(Long id ) {
        logger.info(String.format("Попытка извлечения записи c id = %d из базы данных", id));
        Optional<CustomerEntity> optionalCustomer = repository.findById(id);
        CustomerEntity entity = optionalCustomer.orElseThrow(() -> {
            logger.warn(String.format("Запись c id = %d отсутсвует в базе данных", id));
            return new NotFoundException(String.format("Клиента с id = %d не существует", id));
        });
        logger.info(String.format("Запись c id = %d успешно извлечена из базы данных", id));
        return entity;
    }
}
