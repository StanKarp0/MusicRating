package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.service.impl.AlbumServiceImpl;
import com.stankarp.ratings.utils.YearRangeHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
public class UserServiceTests {

    private static final Logger logger = LoggerFactory.getLogger(AlbumServiceTests.class);

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
        final Performer performer = new Performer("perf1");
        List<Album> albums = IntStream.range(0, 100).mapToObj(i -> {
            Album album = new Album("alb1" + i, 1910 + i, performer);
            album.setAlbumId((long)i);
            return album;
        }).collect(Collectors.toList());
        Mockito.when(albumRepository.findAll()).thenReturn(albums);
    }

    @Test
    public void whenFindRandom_thenReturnRandom() {
        // given
        YearRangeHelper yearRangeHelper = YearRangeHelper.allYears();

        // when
        Collection<Album> list1 = albumService.findRandom(yearRangeHelper);
        Collection<Album> list2 = albumService.findRandom(yearRangeHelper);

        // then
        List<Long> ids1 = list1.stream().map(Album::getAlbumId).collect(Collectors.toList());
        List<Long> ids2 = list2.stream().map(Album::getAlbumId).collect(Collectors.toList());

        assertThat(ids1).doesNotContainSubsequence(ids2);
    }


}
