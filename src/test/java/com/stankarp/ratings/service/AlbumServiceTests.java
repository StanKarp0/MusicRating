package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.AlbumForm;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
public class AlbumServiceTests {

    private static final Logger logger = LoggerFactory.getLogger(AlbumServiceTests.class);

    @TestConfiguration
    static class AlbumServiceImplTestContextConfiguration {

        @Bean
        public AlbumService albumService(AlbumRepository albumRepository, PerformerRepository performerRepository) {
            return new AlbumServiceImpl(albumRepository, performerRepository);
        }

//        @Bean
//        public PasswordEncoder passwordEncoder() {
//            return new BCryptPasswordEncoder();
//        }
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
        Mockito.when(performerRepository.findById(0L)).thenReturn(Optional.of(performer));
        Mockito.when(performerRepository.findById(1L)).thenReturn(Optional.empty());

        List<Album> albums = IntStream.range(0, 100).mapToObj(i -> {
            Album album = new Album("alb1" + i, 1910 + i, performer);
            album.setAlbumId((long)i);
            return album;
        }).collect(Collectors.toList());
        Mockito.when(albumRepository.findAll()).thenReturn(albums);

        Page<Album> albumPage = new PageImpl<>(albums);
        Mockito.when(albumRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(albumPage);
        Mockito.when(albumRepository.findByQuery(Mockito.anyString(), Mockito.any(PageRequest.class))).thenReturn(albumPage);
        Mockito.when(albumRepository.findByPerformerId(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(albumPage);

        List<Integer> decades = IntStream.range(195, 200).boxed().collect(Collectors.toList());
        Mockito.when(albumRepository.findDistinctDecades()).thenReturn(decades);

        List<Integer> years = IntStream.range(2000, 2009).boxed().collect(Collectors.toList());
        Mockito.when(albumRepository.findDistinctByYearBetween(2000, 2009)).thenReturn(years);

        Album album = albums.get(0);
        Mockito.when(albumRepository.findById(0L)).thenReturn(Optional.of(album));
        Mockito.when(albumRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(albumRepository.save(Mockito.any(Album.class))).thenReturn(album);
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

    @Test
    public void whenFindAll_thenReturnAlbums() {
        Page<Album> result = albumService.findAll(PageRequest.of(0, 10));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void whenFindDecades_thenReturnIntegers() {
        List<Integer> result = albumService.findDecades();
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void whenFindYears_thenReturnIntegers() {
        List<Integer> result = albumService.findYears(200);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void whenSaveAlbum_thenReturnIntegers() {
        AlbumForm albumForm = new AlbumForm("a", 1999, 0L, 0L);
        assertThat(albumService.save(albumForm).isPresent()).isTrue();

        albumForm = new AlbumForm("a", 1999, 1L, 0L);
        assertThat(albumService.save(albumForm).isPresent()).isTrue();

        albumForm = new AlbumForm("a", 1999, 0L, 1L);
        assertThat(albumService.save(albumForm).isPresent()).isTrue();

        albumForm = new AlbumForm("a", 1999, 1L, 1L);
        assertThat(albumService.save(albumForm).isPresent()).isFalse();
    }

    @Test
    public void whenFindByQuery_thenReturnAlbums() {
        Page<Album> result = albumService.findByQuery("a", PageRequest.of(0, 10));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void whenFindByPerformerId_thenReturnAlbums() {
        Page<Album> result = albumService.findByPerformerId(1L, PageRequest.of(0, 10));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void whenDelete_thenReturnAlbums() {
        albumService.delete(0L);
        albumService.delete(1L);
    }

}
