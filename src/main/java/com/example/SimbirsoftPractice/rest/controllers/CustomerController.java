package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Заказчики", description = "Просмотр, создание, изменение и удаление клиентов")
public class CustomerController {
    private static final String REQUEST = "Request: %s " +
            "http://localhost:8080/api/customers" + "%s";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomerService service;

    public CustomerController(CustomerService customerService) {
        this.service = customerService;
    }

    @GetMapping
    @Operation(summary = "Список клиентов")
    public ResponseEntity<List<CustomerResponseDto>> getListCustomers() {
        logger.info(String.format(REQUEST, "GET", ""));
        List<CustomerResponseDto> list = service.getListCustomers();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Информация о клиенте")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable Long id,
                                                           Locale locale) {
        logger.info(String.format(REQUEST, "GET", "/" + id));
        CustomerResponseDto customerResponseDto = service.readCustomer(id, locale);
        return ResponseEntity.ok(customerResponseDto);
    }

    @PostMapping
    @Operation(summary = "Создание нового клиента")
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto requestDto,
                                                              Locale locale) {
        logger.info(String.format(REQUEST, "POST", ""));
        CustomerResponseDto customerResponseDto = service.createCustomer(requestDto, locale);
        return ResponseEntity.ok(customerResponseDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение информации о клиенте")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@RequestBody CustomerRequestDto requestDto,
                                                              @PathVariable Long id,
                                                              Locale locale) {
        logger.info(String.format(REQUEST, "PUT", "/" + id));
        CustomerResponseDto customerResponseDto = service.updateCustomer(requestDto, id, locale);
        return ResponseEntity.ok(customerResponseDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление клиента")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id, Locale locale) {
        logger.info(String.format(REQUEST, "DELETE", "/" + id));
        service.deleteCustomer(id, locale);
        return ResponseEntity.accepted().build();
    }

}