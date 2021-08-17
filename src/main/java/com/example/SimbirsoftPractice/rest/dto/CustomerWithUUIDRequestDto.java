package com.example.SimbirsoftPractice.rest.dto;

import java.util.UUID;

public class CustomerWithUUIDRequestDto {
    private String name;
    private UUID uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
