package com.example.SimbirsoftPractice.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentProjectRequestDto {
    private UUID uuid;
    private String name;
    private BigDecimal price;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
