package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.service.AlbumService;
import com.stankarp.ratings.service.impl.AlbumServiceImpl;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
public class AlbumServiceTests {

    @TestConfiguration
    static class AlbumServiceImplTestContextConfiguration {

        @Bean
        public AlbumService albumService(AlbumRepository albumRepository, PerformerRepository performerRepository) {
            return new AlbumServiceImpl(albumRepository, performerRepository);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private AlbumService albumService;

    @MockBean
    private AlbumRepository albumRepository;

    @MockBean
    private PerformerRepository performerRepository;

    @Before
    public void setUp() {
//        Performer performer = new Performer("performer1");
//        Album alex = new Album("alex", 1992, performer);
//
//        Mockito.when(albumRepository.findById(alex.getAlbumId())).thenReturn(Optional.of(alex));
    }

    // write test cases here
    @Test
    public void whenValidName_thenEmployeeShouldBeFound() {
//        String name = "alex";
//
//        assertThat(albumService.findAll(PageRequest.of(0, 1), YearRangeHelper.fromYear(1992))
//                .get().findFirst().map(Album::getName))
//                .isEqualTo(name);
    }
}
