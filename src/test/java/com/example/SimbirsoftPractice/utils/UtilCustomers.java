package com.example.SimbirsoftPractice.utils;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;

import java.util.UUID;

public class UtilCustomers {
    private static final UtilCustomers.CustomerBuilder defaultCustomer = builder()
            .id(1L).name("Name").uuid(UUID.randomUUID());

    public static CustomerResponseDto defaultResponse() {
        return defaultCustomer.buildResponse();
    }

    public static CustomerRequestDto defaultRequest() {
        return defaultCustomer.buildRequest();
    }

    public static CustomerEntity defaultEntity() {
        return defaultCustomer.buildEntity();
    }

    public static CustomerWithUUIDRequestDto defaultRequestPayment() {
        return defaultCustomer.buildRequestPayment();
    }

    public static UtilCustomers.CustomerBuilder builder() {
        return new UtilCustomers.CustomerBuilder();
    }

    private static class CustomerBuilder {
        private Long id;
        private String name;
        private UUID uuid;

        public UtilCustomers.CustomerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UtilCustomers.CustomerBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UtilCustomers.CustomerBuilder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public CustomerEntity buildEntity() {
            CustomerEntity value = new CustomerEntity();
            value.setId(this.id);
            value.setName(this.name);
            value.setUuid(this.uuid);
            return value;
        }

        public CustomerResponseDto buildResponse() {
            CustomerResponseDto value = new CustomerResponseDto();
            value.setId(this.id);
            value.setName(this.name);
            return value;
        }

        public CustomerRequestDto buildRequest() {
            CustomerRequestDto value = new CustomerRequestDto();
            value.setName(this.name);
            return value;
        }

        public CustomerWithUUIDRequestDto buildRequestPayment() {
            CustomerWithUUIDRequestDto value = new CustomerWithUUIDRequestDto();
            value.setName(this.name);
            value.setUuid(this.uuid);
            return value;
        }
    }

}
