package com.example.SimbirsoftPractice.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Данные о клиенте (from back to front")
public class CustomerResponseDto {
    @Schema(description = "ID заказчика")
    private Long id;
    @Schema(description = "Имя заказчика")
    private String name;

    public CustomerResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
