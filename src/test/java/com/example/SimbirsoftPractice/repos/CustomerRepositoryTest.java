package com.example.SimbirsoftPractice.repos;

import com.example.SimbirsoftPractice.utils.UtilCustomers;
import com.example.SimbirsoftPractice.entities.CustomerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

    private CustomerEntity entity = UtilCustomers.defaultEntity();

    @Autowired
    CustomerRepository customerRepository;

    //предварительное сохранение  объекта в БД для дальнейших тестов
    @BeforeEach
    public void testSave() {
        entity.setId(null);
        entity = customerRepository.save(entity);
        assertNotNull(entity.getId());
    }

    @Test
    @Order(2)
    public void testFindAll() {
        List<CustomerEntity> list = customerRepository.findAll();
        assertTrue(list.size() >= 1);
    }

    @Test
    @Order(3)
    public void testFindById() {
        Optional<CustomerEntity> optional = customerRepository.findById(entity.getId());
        assertTrue(optional.isPresent());
    }

    @Test
    @Order(100)
    public void testDelete() {
        long beforeDelete = customerRepository.count();
        customerRepository.deleteById(entity.getId());
        long afterDelete = customerRepository.count();
        assertEquals(1, beforeDelete - afterDelete);
    }
}