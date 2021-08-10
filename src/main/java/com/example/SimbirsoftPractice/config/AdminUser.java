/**
 * Создание пользователя с именем и паролем, указанными в файле конфигурации
 * если он до этого не было еще создан
 * далее от имени этого пользователя можно создавать другие учетные записи
 */
package com.example.SimbirsoftPractice.config;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.repos.UserRepository;
import com.example.SimbirsoftPractice.rest.domain.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class AdminUser implements CommandLineRunner {
    private final UserRepository userRepository;
    private final String name;
    private final String password;
    private final PasswordEncoder encoder;
    private Set<Role> roles = new HashSet<>(Arrays.asList(Role.ROLE_READ_USERS, Role.ROLE_CRUD_USERS)) ;

    public AdminUser(UserRepository userRepository,
                     @Value("${spring.security.admin.username}") String name,
                     @Value("${spring.security.admin.password}") String password,
                     PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.name = name;
        this.password = password;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByName(name).isPresent()) {
            return;
        }
        UserEntity user = new UserEntity();
        user.setName(name);
        user.setPassword(encoder.encode(password));
        user.setRoles(roles);
        userRepository.save(user);
    }
}
