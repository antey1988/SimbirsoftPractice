package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(value = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {
    private CustomerResponseDto responseBody;
    private CustomerRequestDto requestBody;
    private List<CustomerResponseDto> responseBodyList;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService service;

    @BeforeEach
    public void setUp() {
        responseBody = new CustomerResponseDto();
        responseBody.setId(1L);
        responseBody.setName("Name");

        requestBody = new CustomerRequestDto();
        requestBody.setName("Name");

        responseBodyList = List.of(responseBody);
    }

    @Test
    void testGetCustomersThenReturn200WithBody() throws Exception {
        Mockito.when(service.getListCustomers()).thenReturn(responseBodyList);
        MvcResult mvcResult = mvc.perform(get("/api/customers/")).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(response.getStatus(), 200);

        String expected = response.getContentAsString();
        String actual = objectMapper.writeValueAsString(responseBodyList);
        assertEquals(expected, actual);
    }

    @Test
    void testGetCustomerReturn200WithBody() throws Exception {
        Mockito.when(service.readCustomer(Mockito.anyLong(), Mockito.any())).thenReturn(responseBody);
        MvcResult mvcResult = mvc.perform(get("/api/customers/{id}", 1)).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(response.getStatus(), 200);

        String expected = response.getContentAsString();
        String actual = objectMapper.writeValueAsString(responseBody);
        assertEquals(expected, actual);
    }

    @Test
    void testGetCustomerReturn404NotFound() throws Exception {
        Mockito.when(service.readCustomer(Mockito.anyLong(), Mockito.any())).thenThrow(NotFoundException.class);
        MvcResult mvcResult = mvc.perform(get("/api/customers/{id}", 2)).andReturn();

        assertEquals(mvcResult.getResponse().getStatus(), 404);
    }

    @Test
    void testPostCustomerReturn200WithBody() throws Exception {
        Mockito.when(service.createCustomer(Mockito.any(), Mockito.any())).thenReturn(responseBody);
        MvcResult mvcResult = mvc.perform(post("/api/customers")
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType("application/json")).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(response.getStatus(), 200);

        String expected = response.getContentAsString();
        String actual = objectMapper.writeValueAsString(responseBody);
        assertEquals(expected, actual);
    }

    @Test
    void testPutCustomerReturn200WithBody() throws Exception {
        Mockito.when(service.updateCustomer(Mockito.any(), Mockito.anyLong(), Mockito.any())).thenReturn(responseBody);
        MvcResult mvcResult = mvc.perform(put("/api/customers/{id}", 1)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType("application/json")).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(response.getStatus(), 200);

        String expected = response.getContentAsString();
        String actual = objectMapper.writeValueAsString(responseBody);
        assertEquals(expected, actual);
    }

    @Test
    void testPutCustomerReturn404NotFound() throws Exception {
        Mockito.when(service.updateCustomer(Mockito.any(), Mockito.anyLong(), Mockito.any())).thenThrow(NotFoundException.class);
        MvcResult mvcResult = mvc.perform(put("/api/customers/{id}", 2)
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType("application/json")).andReturn();

        assertEquals(mvcResult.getResponse().getStatus(), 404);
    }

    @Test
    void testDeleteCustomerReturn202NotFound() throws Exception {
        Mockito.doNothing().when(service).deleteCustomer(Mockito.anyLong(), Mockito.any());
        MvcResult mvcResult = mvc.perform(delete("/api/customers/{id}", 1)).andReturn();

        assertEquals(mvcResult.getResponse().getStatus(), 202);
    }

    @Test
    void testDeleteCustomerReturn404NotFound() throws Exception {
        Mockito.doThrow(NotFoundException.class).when(service).deleteCustomer(Mockito.anyLong(), Mockito.any());
        MvcResult mvcResult = mvc.perform(delete("/api/customers/{id}", 2)).andReturn();

        assertEquals(mvcResult.getResponse().getStatus(), 404);
    }
}