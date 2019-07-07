package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.*;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.service.AlbumService;
import com.stankarp.ratings.service.impl.AlbumServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations="classpath:test.properties")
public class AlbumRepositoryTests {

    @TestConfiguration
    static class AlbumServiceImplTestContextConfiguration {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlbumRepository albumRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        Performer performer = new Performer("performer");
        entityManager.persist(performer);
        Album album = new Album("album1", 1999, performer);
        entityManager.persist(album);
        entityManager.flush();

        // when
        Page<Album> page = albumRepository.findByPerformerId(performer.getPerformerId(), PageRequest.of(0, 1));
        List<Album> list = page.getContent();

        // then
        assertThat(list.size()).isEqualTo(1);
//        Album result = list.get(0);
//        assertThat(result.getName()).isEqualTo(album.getName());
//        assertThat(page.getTotalPages()).isEqualTo(1);
//        assertThat(result.getDecade()).isEqualTo(199);
//        assertThat(result.getRatings()).isEqualTo(0);
    }

}
