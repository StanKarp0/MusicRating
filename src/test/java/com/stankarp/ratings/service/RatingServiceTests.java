package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.message.request.AlbumForm;
import com.stankarp.ratings.message.request.RatingForm;
import com.stankarp.ratings.repository.AlbumRepository;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.repository.RatingRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.service.impl.AlbumServiceImpl;
import com.stankarp.ratings.service.impl.RatingServiceImpl;
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
public class RatingServiceTests {

    private static final Logger logger = LoggerFactory.getLogger(AlbumServiceTests.class);

    @TestConfiguration
    static class AlbumServiceImplTestContextConfiguration {

        @Bean
        public RatingService albumService(AlbumRepository albumRepository, RatingRepository ratingRepository,
                                          UserRepository userRepository) {
            return new RatingServiceImpl(albumRepository, ratingRepository, userRepository);
        }

//        @Bean
//        public PasswordEncoder passwordEncoder() {
//            return new BCryptPasswordEncoder();
//        }
    }

    @Autowired
    private RatingService ratingService;

    @MockBean
    private AlbumRepository albumRepository;

    @MockBean
    private RatingRepository ratingRepository;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        final Performer performer = new Performer("perf1");
        Album album = new Album("aa", 1999, performer);
        Mockito.when(albumRepository.findById(0L)).thenReturn(Optional.of(album));
        Mockito.when(albumRepository.findById(1L)).thenReturn(Optional.empty());

        String username = "john";
        User user = new User(username, "aaaaaaaavvvv");
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsername("aaa")).thenReturn(Optional.empty());

        List<Rating> ratingList = IntStream.range(0, 100).mapToObj(i -> {
            Rating rating = new Rating(user, 9.1, "aa", album);
            rating.setRatingId((long)i);
            return rating;
        }).collect(Collectors.toList());
        Page<Rating> ratingPage = new PageImpl<>(ratingList);
        Mockito.when(ratingRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(ratingPage);
        Mockito.when(ratingRepository.findByUserName(Mockito.anyString(), Mockito.any(PageRequest.class))).thenReturn(ratingPage);
        Mockito.when(ratingRepository.findByAlbum(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(ratingPage);

        Mockito.when(ratingRepository.findById(0L)).thenReturn(Optional.of(ratingList.get(0)));
        Mockito.when(ratingRepository.findById(1L)).thenReturn(Optional.empty());
    }

    @Test
    public void whenSave_thenReturnRating() {
        RatingForm ratingForm = new RatingForm("a", 2.2, 0L, null);
        Optional<Rating> rating = ratingService.save(ratingForm, "john");
        assertThat(rating.isPresent()).isTrue();

        ratingForm = new RatingForm("a", 2.2, 0L, 0L);
        rating = ratingService.save(ratingForm, "john");
        assertThat(rating.isPresent()).isTrue();

        ratingForm = new RatingForm("a", 2.2, 1L, 0L);
        rating = ratingService.save(ratingForm, "john");
        assertThat(rating.isPresent()).isTrue();

        ratingForm = new RatingForm("a", 2.2, 1L, null);
        rating = ratingService.save(ratingForm, "john");
        assertThat(rating.isPresent()).isFalse();

        rating = ratingService.save(ratingForm, "johns");
        assertThat(rating.isPresent()).isFalse();
    }

    @Test
    public void whenFindAll_thenReturnRating() {
        Page<Rating> result = ratingService.findAll(PageRequest.of(0, 10));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void whenFindByUser_thenReturnRatings() {
        Page<Rating> result = ratingService.findByUser("a", PageRequest.of(0, 10));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void whenFindByAlbumId_thenReturnRatings() {
        Page<Rating> result = ratingService.findByAlbumId(1L, PageRequest.of(0, 10));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void whenFindById_thenReturnRating() {
        Optional<Rating> result = ratingService.findById(0L);
        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
        assertThat(ratingService.findById(1L).isPresent()).isFalse();
    }

    @Test
    public void whenDelete_thenReturnAlbums() {
        ratingService.delete(0L);
        ratingService.delete(1L);
    }



}
