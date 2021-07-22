package com.example.SimbirsoftPractice.rest.dto;

import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "Данные о проекте (from back to front)")
public class ProjectResponseDto {
    @Schema(description = "ID проекта")
    private Long id;
    @Schema(description = "Наименование проекта")
    private String name;
    @Schema(description = "Описание проекта")
    private String description;
    @Schema(description = "Клиент")
    private CustomerResponseDto customer;
    @Schema(description = "Дата начала проекта")
    private Date startDate;
    @Schema(description = "Дата окончания окончания")
    private Date stopDate;
    @Schema(description = "Статус проекта")
    private StatusProject status;

    public ProjectResponseDto() {
    }

    public ProjectResponseDto(Long id, String name, String description, CustomerResponseDto customer, Date startDate, Date stopDate, StatusProject status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.customer = customer;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CustomerResponseDto getCustomer() {
        return customer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public StatusProject getStatus() {
        return status;
    }
}
