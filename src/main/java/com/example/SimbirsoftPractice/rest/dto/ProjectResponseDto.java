package com.example.SimbirsoftPractice.rest.dto;

import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
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
    private Long customer;
    @Schema(description = "Дата начала проекта")
    private Date startDate;
    @Schema(description = "Дата окончания проекта")
    private Date stopDate;
    @Schema(description = "Статус проекта")
    private StatusProject status;
    @Schema(description = "Стоимость проекта")
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public StatusProject getStatus() {
        return status;
    }

    public void setStatus(StatusProject status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
