package com.stankarp.ratings;

import com.stankarp.ratings.entity.*;
import com.stankarp.ratings.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://developer.okta.com/blog/2018/08/22/basic-crud-angular-7-and-spring-boot-2
 * https://docs.spring.io/spring-data/rest/docs/current/reference/html/
 * https://spring.io/guides/tutorials/bookmarks/
 * https://spring.io/guides/gs/rest-service-cors/
 */
@SpringBootApplication
public class RatingsApplication {

    private static final Logger logger = LoggerFactory.getLogger(RatingsApplication.class);

    @Autowired
    private PasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(RatingsApplication.class, args);
	}

    @Bean
	ApplicationRunner init(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {

            Role role1 = roleRepository.findByName(RoleName.ROLE_ADMIN).orElse(new Role(RoleName.ROLE_ADMIN));
            Role role3 = roleRepository.findByName(RoleName.ROLE_USER).orElse(new Role(RoleName.ROLE_USER));

            roleRepository.save(role1);
            roleRepository.save(role3);


            // USERS
            if (!userRepository.findByUsername("admin").isPresent()) {
                User user = new User("admin", encoder.encode("Passw0rd"));
                user.setRoles(Stream.of(role1).collect(Collectors.toSet()));
                userRepository.save(user);
            }

            if (!userRepository.findByUsername("admin-user").isPresent()) {
                User user = new User("admin-user", encoder.encode("Passw0rd"));
                user.setRoles(Stream.of(role1, role3).collect(Collectors.toSet()));
                userRepository.save(user);
            }

            if (!userRepository.findByUsername("test-user").isPresent()) {
                User user = new User("test-user", encoder.encode("Passw0rd"));
                user.setRoles(Stream.of(role3).collect(Collectors.toSet()));
                userRepository.save(user);
            }
        };
	}

}
