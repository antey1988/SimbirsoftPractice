/**
 * Сервис, реализующий интерфейс UserDetailsService
 * поиск пользователя осуществляется в базе данных
 */
package com.example.SimbirsoftPractice.security;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String INFO_TRY_GET_USER = "Поиск пользователя с именем %s в базе данных";
    private static final String WARN_NOT_FOUND_USER = "Пользователь с именем %s не найден в базе данных";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info(String.format(INFO_TRY_GET_USER, s));
        UserEntity user = userRepository.findByName(s)
                .orElseThrow(() -> {
                    String text = String.format(WARN_NOT_FOUND_USER, s);
                    logger.warn(text);
                    return new UsernameNotFoundException(text);
                });
        UserDetails userDetails = User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .authorities(user.getRolesAsString())
                .build();
        return new UserWithId(userDetails, user.getId());
    }
}
