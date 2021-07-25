package com.example.SimbirsoftPractice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Данные о клиенте (from front to back)")
public class CustomerRequestDto {
    @Schema(description = "ID заказчика")
    private Long id;
    @Schema(description = "Имя клиента")
    private String name;

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
}
