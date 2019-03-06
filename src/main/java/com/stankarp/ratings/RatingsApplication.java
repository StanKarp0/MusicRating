package com.stankarp.ratings;

import com.stankarp.ratings.entity.*;
import com.stankarp.ratings.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
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

	public static void main(String[] args) {
		SpringApplication.run(RatingsApplication.class, args);
	}

	@Bean
	ApplicationRunner init(AlbumRepository albumRepository, PerformerRepository performerRepository,
                           RatingRepository ratingRepository, RoleRepository roleRepository,
                           UserRepository userRepository) {
        return args -> {

            // ROLES
            Role role1 = new Role("ADMIN");
            Role role2 = new Role("USER");

            roleRepository.save(role1);
            roleRepository.save(role2);

            // USERS
            User user1 = new User("admin", "admin");
            User user2 = new User("admin-user", "admin");
            User user3 = new User("user", "admin");

            user1.setRoles(Stream.of(role1).collect(Collectors.toSet()));
            user2.setRoles(Stream.of(role1, role2).collect(Collectors.toSet()));
            user3.setRoles(Stream.of(role2).collect(Collectors.toSet()));

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            // PERFORMERS
            Performer performer1 = new Performer("The Beatles");
            Performer performer2 = new Performer("Neil Young");

            performerRepository.save(performer1);
            performerRepository.save(performer2);

            // ALBUMS
            Album album1 = new Album("Revolver", 1966, performer1);
            Album album2 = new Album("White Album", 1969, performer1);
            Album album3 = new Album("Harvest", 1972, performer2);
            Album album4 = new Album("On the Beach", 1974, performer2);
            Album album5 = new Album("On the Sand", 1970, performer2);

            albumRepository.save(album1);
            albumRepository.save(album2);
            albumRepository.save(album3);
            albumRepository.save(album4);
            albumRepository.save(album5);

            // RATINGS
            ratingRepository.save(new Rating(user1, 9.0, "", album1));
            ratingRepository.save(new Rating(user1, 8.0, "", album2));
            ratingRepository.save(new Rating(user1, 7.0, "", album3));
//            ratingRepository.save(new Rating(user1, 6.0, "", album4));
            ratingRepository.save(new Rating(user2, 5.0, "", album1));
            ratingRepository.save(new Rating(user2, 4.0, "", album2));
            ratingRepository.save(new Rating(user2, 3.0, "", album3));
            ratingRepository.save(new Rating(user3, 2.0, "", album1));
            ratingRepository.save(new Rating(user3, 1.0, "", album2));

        };
	}

}
