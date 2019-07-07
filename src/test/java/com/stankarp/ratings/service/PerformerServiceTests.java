package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.PerformerAlbumForm;
import com.stankarp.ratings.message.request.PerformerForm;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.service.impl.PerformerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations="classpath:test.properties")
public class PerformerServiceTests {

    @TestConfiguration
    static class AlbumServiceImplTestContextConfiguration {

        @Bean
        public PerformerService performerService(AlbumRepository albumRepository, PerformerRepository performerRepository) {
            return new PerformerServiceImpl(performerRepository, albumRepository);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PerformerService performerService;

    @Autowired
    private PerformerRepository performerRepository;

    @Test
    public void whenSave_thenPerformerShouldBeFound() {
        PerformerAlbumForm albumForm = new PerformerAlbumForm();
        albumForm.setTitle("asd1");
        albumForm.setYear(1992);

        final String name = "test123";
        PerformerForm performerForm = new PerformerForm();
        performerForm.setName(name);

        final List<PerformerAlbumForm> albums = new LinkedList<>();
        albums.add(albumForm);
        performerForm.setAlbums(albums);

        Performer performerResult = performerService.save(performerForm);
        assertThat(performerResult.getAlbumCount()).isEqualTo(albums.size());
        assertThat(performerResult.getName()).isEqualTo(name);

        Page<Performer> page = performerRepository.findByQuery(name, PageRequest.of(0, 10));
        assertThat(page.getTotalPages()).isGreaterThan(0);
        Optional<Performer> optionalPerformer = page.get().findAny();
        assertThat(optionalPerformer.isPresent()).isTrue();
        optionalPerformer.ifPresent(performer -> {
            assertThat(performer.getAlbumCount()).isEqualTo(albums.size());
            assertThat(performer.getName()).isEqualTo(name);
        });
    }

    @Test
    public void whenUpdate_thenPerformerShouldBeUpdated() {
        PerformerAlbumForm albumForm = new PerformerAlbumForm();
        albumForm.setTitle("asd1");
        albumForm.setYear(1992);

        final String name = "test123";
        PerformerForm performerForm = new PerformerForm();
        performerForm.setName(name);

        final List<PerformerAlbumForm> albums = new LinkedList<>();
        albums.add(albumForm);
        performerForm.setAlbums(albums);

        Performer performerResult = performerService.save(performerForm);
        assertThat(performerResult.getAlbumCount()).isEqualTo(albums.size());
        assertThat(performerResult.getName()).isEqualTo(name);

        Page<Performer> page = performerRepository.findByQuery(name, PageRequest.of(0, 10));
        assertThat(page.getTotalPages()).isGreaterThan(0);
        Optional<Performer> optionalPerformer = page.get().findAny();
        assertThat(optionalPerformer.isPresent()).isTrue();
        optionalPerformer.ifPresent(performer -> {
            assertThat(performer.getAlbumCount()).isEqualTo(albums.size());
            assertThat(performer.getName()).isEqualTo(name);
        });
    }

}
