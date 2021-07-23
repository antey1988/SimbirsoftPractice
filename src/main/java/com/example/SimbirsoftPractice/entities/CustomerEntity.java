package com.example.SimbirsoftPractice.entities;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public CustomerEntity() {
    }

    public CustomerEntity(String name) {
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
