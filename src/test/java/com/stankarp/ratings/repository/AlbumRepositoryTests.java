package com.stankarp.ratings.repository;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations="classpath:test.properties")
public class AlbumRepositoryTests {

    private static final Logger logger = LoggerFactory.getLogger(AlbumRepositoryTests.class);

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
    public void whenFindByPerformerId_thenReturnAlbumPage() {
        // given
        Performer performer1 = new Performer("performer1");
        entityManager.persist(performer1);
        entityManager.persist(new Album("album1", 1999, performer1));

        Performer performer2 = new Performer("performer2");
        entityManager.persist(performer2);
        entityManager.persist(new Album("album2", 1999, performer2));
        entityManager.persist(new Album("album3", 1998, performer2));
        entityManager.flush();

        // when
        Page<Album> page1 = albumRepository.findByPerformerId(performer1.getPerformerId(), PageRequest.of(0, 10));
        Page<Album> page2 = albumRepository.findByPerformerId(performer2.getPerformerId(), PageRequest.of(0, 10));

        // then
        assertThat(page1.getTotalElements()).isEqualTo(1);
        assertThat(page2.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void whenFindByQuery_thenReturnAlbumPage() {
        // given
        Performer performer1 = new Performer("performer3");
        entityManager.persist(performer1);
        entityManager.persist(new Album("dddaaasss", 1999, performer1));
        entityManager.persist(new Album("aAasdaAa", 1999, performer1));

        Performer performer2 = new Performer("performer4");
        entityManager.persist(performer2);
        entityManager.persist(new Album("sdaAasss", 1999, performer2));
        entityManager.persist(new Album("dddddd", 1998, performer2));
        entityManager.flush();

        // when
        Page<Album> page1 = albumRepository.findByQuery("AAA", PageRequest.of(0, 10));
        Page<Album> page2 = albumRepository.findByQuery("DDD", PageRequest.of(0, 10));

        // then
        assertThat(page1.getTotalElements()).isEqualTo(3);
        assertThat(page2.getTotalElements()).isEqualTo(2);

    }

    @Test
    public void whenFindDistinctByYearBetween_thenReturnDistinct() {
        // given
        Performer performer1 = new Performer("performer5");
        entityManager.persist(performer1);
        entityManager.persist(new Album("dddaa4", 2231, performer1));
        entityManager.persist(new Album("aAasd3", 2240, performer1));
        entityManager.persist(new Album("asasa2", 2241, performer1));
        entityManager.persist(new Album("aaaaa1", 2230, performer1));
        entityManager.persist(new Album("aaaba1", 2231, performer1));
        entityManager.flush();

        // when
        List<Integer> years1 = albumRepository.findDistinctByYearBetween(2230, 2240);
        List<Integer> years2 = albumRepository.findDistinctByYearBetween(2231, 2240);
        List<Integer> years3 = albumRepository.findDistinctByYearBetween(2231, 2241);

        // then
        assertThat(years1).contains(2230, 2231, 2240);
        assertThat(years2).contains(2231, 2240);
        assertThat(years3).contains(2231, 2240, 2241);

        assertThat(years1.size()).isEqualTo(3);
        assertThat(years2.size()).isEqualTo(2);
        assertThat(years3.size()).isEqualTo(3);

    }

    @Test
    public void whenFindDistinctDecades_thenReturnDistinct() {
        // given
        Performer performer1 = new Performer("performer5");
        entityManager.persist(performer1);
        entityManager.persist(new Album("dddaa4", 2331, performer1));
        entityManager.persist(new Album("aAasd3", 2340, performer1));
        entityManager.persist(new Album("asasa2", 2341, performer1));
        entityManager.persist(new Album("aaaaa1", 2330, performer1));
        entityManager.persist(new Album("aaaba1", 2331, performer1));
        entityManager.flush();

        // when
        List<Integer> decades = albumRepository.findDistinctDecades();

        // then
        assertThat(decades).contains(233, 234);
        assertThat(decades).doesNotContain(232);

    }

}
