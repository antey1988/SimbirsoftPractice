package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Заказчики", description = "Просмотр, создание, изменение и удаление клиентов")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService customerService) {
        this.service = customerService;
    }

    @GetMapping
    @Operation(summary = "Список клиентов")
    public ResponseEntity<List<CustomerResponseDto>> getListCustomers(@RequestHeader HttpHeaders headers) {
        List<CustomerResponseDto> list = service.getListCustomers();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Информация о клиенте")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable Long id) {
        CustomerResponseDto customerResponseDto = service.readCustomer(id);
        return ResponseEntity.ok(customerResponseDto);
    }

    @PostMapping
    @Operation(summary = "Создание нового клиента")
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto requestDto) {
        CustomerResponseDto customerResponseDto = service.createCustomer(requestDto);
        return ResponseEntity.ok(customerResponseDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение информации о клиенте")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@RequestBody CustomerRequestDto requestDto,
                                                      @PathVariable Long id) {
        CustomerResponseDto customerResponseDto = service.updateCustomer(requestDto, id);
        return ResponseEntity.ok(customerResponseDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление клиента")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.accepted().build();
    }
}
