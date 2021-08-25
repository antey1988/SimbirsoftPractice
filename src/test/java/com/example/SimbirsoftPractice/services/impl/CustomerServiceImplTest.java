package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.mappers.CustomerMapper;
import com.example.SimbirsoftPractice.repos.CustomerRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;
import com.example.SimbirsoftPractice.services.PaymentService;
import com.example.SimbirsoftPractice.services.validators.CustomerValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    private final Long id_1 = 1L;
    private final Long id_2 = 2L;

    @Mock
    CustomerRepository repository;
    @Mock
    CustomerMapper mapper;
    @Mock
    CustomerValidatorService validator;
    @Mock
    PaymentService paymentService;
    @Mock
    MessageSource messageSource;

    @InjectMocks
    CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(repository.findById(id_1)).thenReturn(Optional.of(new CustomerEntity()));
        Mockito.lenient().when(mapper.entityToResponseDto(Mockito.any())).thenReturn(new CustomerResponseDto());
    }

    @Test
    void readCustomer() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        assertNotNull(customerService.readCustomer(id_1, null));

        Mockito.when(repository.findById(id_2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> customerService.readCustomer(id_2, null));
    }

    @Test
    void createCustomer() {
        CustomerEntity entity = new CustomerEntity();
        CustomerWithUUIDRequestDto request = new CustomerWithUUIDRequestDto();

        Mockito.when(validator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(entity);
        Mockito.when(repository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.entityToResponseDtoWithUUID(entity)).thenReturn(request);
        Mockito.doNothing().when(paymentService).createClient(request, null);

        assertNotNull(customerService.createCustomer(new CustomerRequestDto(), null));
    }

    @Test
    void updateCustomer() {
        CustomerEntity entity = new CustomerEntity();

        Mockito.when(validator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(entity);

        assertNotNull(customerService.updateCustomer(new CustomerRequestDto(), id_1, null));
    }

    @Test
    void deleteCustomer() {
        Mockito.doNothing().when(repository).deleteById(id_1);
        customerService.deleteCustomer(id_1, null);
        Mockito.verify(repository).deleteById(id_1);
    }

    @Test
    void getListCustomers() {
        List<CustomerEntity> list = List.of(new CustomerEntity(), new CustomerEntity());
        Mockito.when(repository.findAll()).thenReturn(list);
        Mockito.when(mapper.listEntityToListResponseDto(list))
                .thenReturn(List.of(new CustomerResponseDto(), new CustomerResponseDto()));
        List<CustomerResponseDto> response = customerService.getListCustomers();
        assertNotNull(response);
        assertEquals(response.size(), 2);
    }
}