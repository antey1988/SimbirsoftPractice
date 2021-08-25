package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.config.WebSecurity;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(value = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService service;

    @Test
    void getListCustomers() throws Exception {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setId(1L);
        response.setName("Name");
        Mockito.when(service.getListCustomers()).thenReturn(List.of(response));
        mvc.perform(MockMvcRequestBuilders.get("/api/customers/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getCustomer() throws Exception {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setId(1L);
        response.setName("Name");

        Mockito.when(service.readCustomer(1L, null)).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get("/api/customers/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.when(service.readCustomer(2L, null)).thenThrow(NotFoundException.class);
        mvc.perform(MockMvcRequestBuilders.get("/api/customers/2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomer() {
    }
}