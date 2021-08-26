package com.example.SimbirsoftPractice.repos;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/simbirsoft",
        "spring.datasource.username=postgres",
        "spring.datasource.password=postgres",
        "spring.jpa.properties.hibernate.format_sql=true"})
class CustomerRepositoryTest {

    private CustomerEntity entity;

    @Autowired
    CustomerRepository customerRepository;

    //предварительное сохранение  объекта в БД для дальнейших тестов
    @BeforeEach
    public void setUp() {
        entity = new CustomerEntity();
        entity.setName("Name");
        entity.setUuid(UUID.randomUUID());
        entity = customerRepository.save(entity);
        assertNotNull(entity.getId());
    }

    @Test
    public void testFindAll() {
        List<CustomerEntity> list = customerRepository.findAll();
        assertTrue(list.size() >= 1);
    }

    @Test
    public void testFindById() {
        Optional<CustomerEntity> optional = customerRepository.findById(entity.getId());
        assertTrue(optional.isPresent());
    }

    @Test
    public void testDelete() {
        long beforeDelete = customerRepository.count();
        customerRepository.deleteById(entity.getId());
        long afterDelete = customerRepository.count();
        assertEquals(1, beforeDelete - afterDelete);
    }
}