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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*").allowedOrigins("http://localhost:4200");
            }
        };
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    String randomString(int len){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    @Bean
	ApplicationRunner init(AlbumRepository albumRepository, PerformerRepository performerRepository,
                           RatingRepository ratingRepository, RoleRepository roleRepository,
                           UserRepository userRepository) {
        return args -> {

            // ROLES
            Role role1 = new Role(RoleName.ROLE_ADMIN);
            Role role2 = new Role(RoleName.ROLE_PM);
            Role role3 = new Role(RoleName.ROLE_USER);

            roleRepository.save(role1);
            roleRepository.save(role2);
            roleRepository.save(role3);

            // USERS
            User user1 = new User("admin", encoder.encode("admin123"));
            User user2 = new User("admin-user", encoder.encode("admin-user123"));
            User user3 = new User("user", encoder.encode("user123"));
            List<User> users = Stream.of(user1, user2, user3).collect(Collectors.toList());

            user1.setRoles(Stream.of(role1).collect(Collectors.toSet()));
            user2.setRoles(Stream.of(role1, role3).collect(Collectors.toSet()));
            user3.setRoles(Stream.of(role3).collect(Collectors.toSet()));

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            // PERFORMERS
            Performer performer1 = new Performer("The Beatles");
            Performer performer2 = new Performer("Neil Young");
            Performer performer3 = new Performer("Pink Floyd");
            Performer performer4 = new Performer("Cure");
            List<Performer> performers = Stream.of(performer1, performer2, performer3, performer4)
                    .collect(Collectors.toList());

            performerRepository.save(performer1);
            performerRepository.save(performer2);
            performerRepository.save(performer3);
            performerRepository.save(performer4);

            // ALBUMS
            for (int i = 0; i < 50; i++) {
                String title = randomString(10);
                Performer performer = performers.get(rnd.nextInt(performers.size()));
                Album album = new Album(title, rnd.nextInt(50) + 1950, performer);
                albumRepository.save(album);
                performer.addAlbum(album);

                for (int j = 0; j < rnd.nextInt(3); j ++) {
                    User user = users.get(rnd.nextInt(users.size()));
                    Rating rating = new Rating(user, rnd.nextDouble() * 10., "", album);
                    ratingRepository.save(rating);
                    album.addRating(rating);
                }
                albumRepository.save(album);
                performerRepository.save(performer);

            }
        };
	}

}
