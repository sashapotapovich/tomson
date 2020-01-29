package com.vaadin.service;

import com.vaadin.entity.User;
import com.vaadin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class InsertUsers {

    @Bean(name = "UserCreation")
    public CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            User admin = userRepository.findByEmailIgnoreCase("admin@admin.com");
            User user = userRepository.findByEmailIgnoreCase("user@user.com");
            if (admin == null) {
                log.info("Preloading " + userRepository.save(new User("admin@admin.com", passwordEncoder.encode("password"),
                                                                      "admin", "admin", "ROLE_ADMIN", false)));
            } else {
                log.info("User \"admin@admin.com\" already exist in the database");
            }
            if (user == null) {
                log.info("Preloading " + userRepository.save(new User("user@user.com", passwordEncoder.encode("password"),
                                                                      "user", "user", "ROLE_USER", false)));
            } else {
                log.info("User \"user@user.com\" already exist in the database");
            }
        };
    }

}
