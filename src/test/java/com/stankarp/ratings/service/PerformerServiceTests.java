package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.PerformerAlbumForm;
import com.stankarp.ratings.message.request.PerformerForm;
import com.stankarp.ratings.message.request.PerformerUpdateForm;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.service.impl.PerformerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
public class PerformerServiceTests {

    @TestConfiguration
    static class PerformerServiceTestsContextConfiguration {

        @Bean
        public PerformerService performerService(AlbumRepository albumRepository, PerformerRepository performerRepository) {
            return new PerformerServiceImpl(performerRepository, albumRepository);
        }

    }

    @Autowired
    private PerformerService performerService;

    @MockBean
    private PerformerRepository performerRepository;

    @MockBean
    private AlbumRepository albumRepository;

    @Before
    public void setUp() {
        final Performer performer = new Performer("perf1");
        Mockito.when(performerRepository.findById(0L)).thenReturn(Optional.of(performer));
        Mockito.when(performerRepository.findById(1L)).thenReturn(Optional.empty());

        List<Performer> performersList = IntStream.range(0, 100).mapToObj(i -> {
            Performer p = new Performer("perf" + i);
            p.setPerformerId((long)i);
            return p;
        }).collect(Collectors.toList());

        Page<Performer> performerPage = new PageImpl<>(performersList);
        Mockito.when(performerRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(performerPage);
        Mockito.when(performerRepository.findByQuery(Mockito.anyString(), Mockito.any(PageRequest.class))).thenReturn(performerPage);
    }

    @Test
    public void whenSave_thenPerformerShouldBeFound() {
        // given
        PerformerAlbumForm albumForm = new PerformerAlbumForm();
        albumForm.setTitle("asd1");
        albumForm.setYear(1992);

        final String name = "tests123";
        PerformerForm performerForm = new PerformerForm();
        performerForm.setName(name);

        final List<PerformerAlbumForm> albums = new LinkedList<>();
        albums.add(albumForm);
        performerForm.setAlbums(albums);

        // when
        Performer performerResult = performerService.save(performerForm);

        // then
        assertThat(performerResult.getName()).isEqualTo(name);
    }

    @Test
    public void whenUpdate_thenPerformerShouldBeUpdated() {
        // given
        final String name = "test1234";
        final String updated = "1234test";
        PerformerForm performerForm = new PerformerForm();
        performerForm.setName(name);

        PerformerUpdateForm updateForm = new PerformerUpdateForm();
        updateForm.setPerformerId(0L);
        updateForm.setName(updated);

        // when
        performerService.update(updateForm).ifPresent(performer ->
                // then
                assertThat(performer.getName()).isEqualTo(updated)
        );
    }

    @Test
    public void whenFindAll_thenReturnPerformers() {
        Page<Performer> result = performerService.findAll(PageRequest.of(0, 10));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void whenFindByQuery_thenReturnPerformers() {
        Page<Performer> result = performerService.findByQuery("a", PageRequest.of(0, 10));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void whenFindById_thenReturnPerformer() {
        assertThat(performerService.findById(0L).isPresent()).isTrue();
        assertThat(performerService.findById(1L).isPresent()).isFalse();
    }

    @Test
    public void whenDelete_thenDeletePerformer() {
        performerService.delete(0L);
        performerService.delete(1L);
    }

}
