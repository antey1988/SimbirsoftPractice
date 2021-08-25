package com.example.SimbirsoftPractice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

//    public WebSecurity(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                //для доступа через swagger
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**")
                    .authenticated()

                //просматривать пользователей могут пользователи с ролями "ROLE_READ_USERS" и "ROLE_CRUD_USERS"
                .antMatchers(HttpMethod.GET, "/api/users/**")
                    .hasAnyAuthority("ROLE_READ_USERS", "ROLE_CRUD_USERS")

                //создавать, редактировать и удалять пользователей могут пользователи с ролями "ROLE_CRUD_USERS"
                .antMatchers("/api/users/**")
                    .hasAuthority("ROLE_CRUD_USERS")

                //создавать, просматривать, редактировать и удалять клиентов могут пользователи с ролями "ROLE_CRUD_CUSTOMERS"
                .antMatchers("/api/customers/**")
                    .hasAuthority("ROLE_CRUD_CUSTOMERS")

                //просматривать проекты, релизы и задачи могут пользователи с ролями "ROLE_READ_OTHERS" и "ROLE_CRUD_OTHERS"
                .antMatchers(HttpMethod.GET, "/api/projects/**", "/api/releases/**","/api/tasks/**")
                .hasAnyAuthority("ROLE_READ_OTHERS", "ROLE_CRUD_OTHERS")

                //создавать, просматривать проекты, релизы и задачи могут пользователи с ролями "ROLE_CRUD_OTHERS"
                .antMatchers( "/api/projects/**", "/api/releases/**","/api/tasks/**")
                    .hasAuthority("ROLE_CRUD_OTHERS")

                .and().httpBasic()
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        /*auth.inMemoryAuthentication()
//                .withUser("Leader")
//                .password("{noop}12345")
//                .authorities("ROLE_READ_USERS", "ROLE_CRUD_CUSTOMERS", "ROLE_CRUD_OTHERS")
//                .and()
//                .withUser("User")
//                .password("{noop}12345")
//                .authorities("ROLE_READ_OTHERS")
//                .and()
//                .withUser("Admin")
//                .password("{noop}12345")
//                .authorities("ROLE_CRUD_USERS");*/
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
