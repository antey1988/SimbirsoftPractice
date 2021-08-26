package com.example.SimbirsoftPractice.configurations;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;

import java.math.BigDecimal;
import java.util.Date;

public class UtilProjects {
    private static UtilProjects.ProjectBuilder defaultProject = UtilProjects.builder()
            .id(1L).name("Name").description("Description").customer(UtilCustomers.defaultEntity())
            .status(StatusProject.CREATED).startDate(new Date()).stopDate(new Date())
            .price(new BigDecimal("10.00"));

    public static ProjectResponseDto defaultResponse() {
        return defaultProject.buildResponse();
    }

    public static ProjectRequestDto defaultRequest() {
        return defaultProject.buildRequest();
    }

    public static ProjectEntity defaultEntity() {
        return defaultProject.buildEntity();
    }

    public static PaymentProjectRequestDto defaultRequestPayment() {
        return defaultProject.buildRequestPayment();
    }

    public static UtilProjects.ProjectBuilder builder() {
        return new UtilProjects.ProjectBuilder();
    }

    private static class ProjectBuilder {
        private Long id;
        private String name;
        private String description;
        private CustomerEntity customer;
        private StatusProject status;
        private Date start;
        private Date stop;
        private BigDecimal price;

        public UtilProjects.ProjectBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UtilProjects.ProjectBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UtilProjects.ProjectBuilder description(String description) {
            this.description = description;
            return this;
        }

        public UtilProjects.ProjectBuilder customer(CustomerEntity customer) {
            this.customer = customer;
            return this;
        }

        public UtilProjects.ProjectBuilder status(StatusProject status) {
            this.status = status;
            return this;
        }

        public UtilProjects.ProjectBuilder startDate(Date date) {
            this.start = date;
            return this;
        }

        public UtilProjects.ProjectBuilder stopDate(Date date) {
            this.stop = date;
            return this;
        }

        public UtilProjects.ProjectBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProjectEntity buildEntity() {
            ProjectEntity value = new ProjectEntity();
            value.setId(this.id);
            value.setName(this.name);
            value.setDescription(this.description);
            value.setCustomer(this.customer);
            value.setStartDate(this.start);
            value.setStopDate(this.stop);
            value.setStatus(this.status);
            value.setPrice(this.price);
            return value;
        }

        public ProjectResponseDto buildResponse() {
            ProjectResponseDto value = new ProjectResponseDto();
            value.setId(this.id);
            value.setName(this.name);
            value.setDescription(this.description);
            value.setCustomer(this.customer.getId());
            value.setStartDate(this.start);
            value.setStopDate(this.stop);
            value.setStatus(this.status);
            value.setPrice(this.price);
            return value;
        }

        public ProjectRequestDto buildRequest() {
            ProjectRequestDto value = new ProjectRequestDto();
            value.setName(this.name);
            value.setDescription(this.description);
            value.setCustomer(this.customer.getId());
            value.setStatus(this.status);
            value.setPrice(this.price);
            return value;
        }

        public PaymentProjectRequestDto buildRequestPayment() {
            PaymentProjectRequestDto value = new PaymentProjectRequestDto();
            value.setName(this.name);
            value.setPrice(this.price);
            value.setUuid(this.customer.getUuid());
            return value;
        }
    }

}
