package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.domain.Role;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Заказчики", description = "Просмотр, создание, изменение и удаление клиентов")
public class CustomerController {

    @GetMapping
    @Operation(summary = "Список клиентов")
    public ResponseEntity<List<CustomerResponseDto>> getCustomers() {
        List<CustomerResponseDto> list = new ArrayList<>();
        list.add(new CustomerResponseDto(1L, "Name1"));
        list.add(new CustomerResponseDto(2L, "Name2"));
        list.add(new CustomerResponseDto(3L, "Name3"));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Создание клиента")
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto requestDto) {
       return ResponseEntity.ok()
                .body(new CustomerResponseDto(1L, requestDto.getName()));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение данных клиента")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@RequestBody CustomerRequestDto requestDto,
                                                      @PathVariable Long id) {
        return ResponseEntity.ok()
                .body(new CustomerResponseDto(id, requestDto.getName()));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление клиента")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
