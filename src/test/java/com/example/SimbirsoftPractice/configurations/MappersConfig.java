package com.example.SimbirsoftPractice.configurations;

import com.example.SimbirsoftPractice.mappers.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MappersConfig {
    @Bean
    public CustomerMapperImpl customerMapper() {
        return new CustomerMapperImpl();
    }
    @Bean
    public ProjectMapperImpl projectMapper() {
        return new ProjectMapperImpl();
    }
    @Bean
    public ReleaseMapperImpl releaseMapper() {
        return new ReleaseMapperImpl();
    }
    @Bean
    public UserMapperImpl userMapper() {
        return new UserMapperImpl();
    }
    @Bean
    public TaskMapperImpl taskMapper() {
        return new TaskMapperImpl();
    }
}
