package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Заказчики", description = "Просмотр, создание, изменение и удаление клиентов")
public class CustomerController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomerService service;

    public CustomerController(CustomerService customerService) {
        this.service = customerService;
    }

    @GetMapping
    @Operation(summary = "Список клиентов")
    public ResponseEntity<List<CustomerResponseDto>> getListCustomers(@RequestHeader HttpHeaders headers) {
        logger.info("Выполнен запрос на получение списка клиентов");
        List<CustomerResponseDto> list = service.getListCustomers();
        logger.info("Список клиентов получен");
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Информация о клиенте")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на получение информации о клиенте c id = %d", id));
        CustomerResponseDto customerResponseDto = service.readCustomer(id);
        logger.info(String.format("Информация о клиенте с id = %d получена", id));
        return ResponseEntity.ok(customerResponseDto);
    }

    @PostMapping
    @Operation(summary = "Создание нового клиента")
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto requestDto) {
        logger.info("Выполнен запрос на создание нового клиента");
        CustomerResponseDto customerResponseDto = service.createCustomer(requestDto);
        logger.info("Новый клиент создан");
        return ResponseEntity.ok(customerResponseDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение информации о клиенте")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@RequestBody CustomerRequestDto requestDto,
                                                      @PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на изменение информации о клиенте c id = %d", id));
        CustomerResponseDto customerResponseDto = service.updateCustomer(requestDto, id);
        logger.info(String.format("Информация о клиенте c id = %d успешно обновлена", id));
        return ResponseEntity.ok(customerResponseDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление клиента")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на удаление клиента c id = %d", id));
        service.deleteCustomer(id);
        logger.info(String.format("Клиент c id = %d успешно удален", id));
        return ResponseEntity.accepted().build();
    }
}
