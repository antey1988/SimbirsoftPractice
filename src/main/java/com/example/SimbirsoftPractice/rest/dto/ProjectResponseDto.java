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

    public ProjectResponseDto() {
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Long getCustomer() {
        return this.customer;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getStopDate() {
        return this.stopDate;
    }

    public StatusProject getStatus() {
        return this.status;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public void setStatus(StatusProject status) {
        this.status = status;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProjectResponseDto)) return false;
        final ProjectResponseDto other = (ProjectResponseDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$customer = this.getCustomer();
        final Object other$customer = other.getCustomer();
        if (this$customer == null ? other$customer != null : !this$customer.equals(other$customer)) return false;
        final Object this$startDate = this.getStartDate();
        final Object other$startDate = other.getStartDate();
        if (this$startDate == null ? other$startDate != null : !this$startDate.equals(other$startDate)) return false;
        final Object this$stopDate = this.getStopDate();
        final Object other$stopDate = other.getStopDate();
        if (this$stopDate == null ? other$stopDate != null : !this$stopDate.equals(other$stopDate)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null ? other$price != null : !this$price.equals(other$price)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ProjectResponseDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $customer = this.getCustomer();
        result = result * PRIME + ($customer == null ? 43 : $customer.hashCode());
        final Object $startDate = this.getStartDate();
        result = result * PRIME + ($startDate == null ? 43 : $startDate.hashCode());
        final Object $stopDate = this.getStopDate();
        result = result * PRIME + ($stopDate == null ? 43 : $stopDate.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        return result;
    }

    public String toString() {
        return "ProjectResponseDto(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", customer=" + this.getCustomer() + ", startDate=" + this.getStartDate() + ", stopDate=" + this.getStopDate() + ", status=" + this.getStatus() + ", price=" + this.getPrice() + ")";
    }
}
