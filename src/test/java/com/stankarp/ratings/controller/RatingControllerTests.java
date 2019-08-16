package com.stankarp.ratings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.entity.Rating;
import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.message.request.RatingForm;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.security.jwt.JwtAuthEntryPoint;
import com.stankarp.ratings.security.jwt.JwtProvider;
import com.stankarp.ratings.security.services.UserDetailsServiceImpl;
import com.stankarp.ratings.service.RatingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RatingController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class RatingControllerTests {

    @TestConfiguration
    static class RatingServiceImplTestContextConfiguration {

        @Bean
        public JwtAuthEntryPoint jwtAuthEntryPoint() {
            return new JwtAuthEntryPoint();
        }

        @Bean
        public JwtProvider jwtProvider() {
            return new JwtProvider();
        }

    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RatingService ratingService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    public void givenRatings_whenGetAll_thenOk()
            throws Exception {
        int page = 0, size = 10;
        List<Rating> ratings = getRatings("john");
        given(ratingService.findAll(PageRequest.of(0, 10))).willReturn(new PageImpl<>(ratings));

        mvc.perform(get("/ratings")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._embedded.ratings", hasSize(ratings.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void givenRating_whenCorrectUsername_thenOk()
            throws Exception {

        int page = 0, size = 10;
        String username = "john";
        List<Rating> ratings = getRatings(username);
        Page<Rating> ratingPage = new PageImpl<>(ratings);
        given(ratingService.findByUser(username, PageRequest.of(0, 10))).willReturn(ratingPage);

        mvc.perform(get("/ratings/user")
                .param("user", username)
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._embedded.ratings", hasSize(ratings.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void givenRating_whenBadUsername_thenNotOk()
            throws Exception {
        String username = "johns";

        mvc.perform(get("/ratings/user")
                .param("user", username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenRatings_whenFindByAlbum_thenOk()
            throws Exception {

        int page = 0, size = 10;
        String username = "john";
        List<Rating> ratings = getRatings(username);
        Page<Rating> ratingPage = new PageImpl<>(ratings);
        given(ratingService.findByAlbumId(1L, PageRequest.of(0, 10))).willReturn(ratingPage);

        mvc.perform(get("/ratings/album")
                .param("albumId", "1")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._embedded.ratings", hasSize(ratings.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void givenRating_whenBadAlbumId_thenNotOk()
            throws Exception {
        mvc.perform(get("/ratings/album")
                .param("album", "aa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenRating_whenNotExistingAlbumId_thenNotOk()
            throws Exception {
        mvc.perform(get("/ratings/album")
                .param("album", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenRating_whenFindById_thenOk()
            throws Exception {
        long ratingId = 1L;
        Rating rating = getRatings("john").get(0);
        rating.setRatingId(ratingId);
        given(ratingService.findById(ratingId)).willReturn(Optional.of(rating));

        mvc.perform(get("/ratings/" + ratingId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER")))
                .andExpect(jsonPath("$.ratingId", is((int)ratingId)))
                .andExpect(status().isOk());
    }

    @Test
    public void givenBadRatingId_whenFindById_thenNotOk()
            throws Exception {
        mvc.perform(get("/ratings/rr")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNotExistingRatingId_whenFindById_thenNotOk()
            throws Exception {
        mvc.perform(get("/ratings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoRights_whenFindById_thenNotOk()
            throws Exception {
        mvc.perform(get("/ratings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenRating_whenSaveRating_thenReturnJsonObject()
            throws Exception {
        Rating rating = getRatings("john").get(0);
        RatingForm form = new RatingForm(rating.getDescription(), rating.getRate(), 1L, 1L,
                rating.getUserName());

        given(ratingService.save(form)).willReturn(Optional.of(rating));

        mvc.perform(post("/ratings")
                .content(objectMapper.writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER"))
        ).andExpect(status().isOk());
    }

    @Test
    public void givenRatingForm_whenCanotSave_thenNotOk()
            throws Exception {
        RatingForm form = new RatingForm("d", 3.3, 1L, 1L, "user");

        given(ratingService.save(ArgumentMatchers.any(RatingForm.class))).willReturn(Optional.empty());

        mvc.perform(post("/ratings")
                .content(objectMapper.writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER"))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenRatingForm_whenNoCred_thenNotOk()
            throws Exception {
        RatingForm form = new RatingForm("d", 3.3, 1L, 1L, "user");

        given(ratingService.save(ArgumentMatchers.any(RatingForm.class))).willReturn(Optional.empty());

        mvc.perform(post("/ratings")
                .content(objectMapper.writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNotValidRatingForm_whenSaveRating_thenNotOk()
            throws Exception {
        List<RatingForm> ratingForms = new LinkedList<>();
        ratingForms.add(new RatingForm(null, 3.3, 1L, 1L, "user"));
        ratingForms.add(new RatingForm("", 3.3, 1L, 1L, "user"));
        ratingForms.add(new RatingForm("d", -1.3, 1L, 1L, "user"));
        ratingForms.add(new RatingForm("d", 10.3, 1L, 1L, "user"));
        ratingForms.add(new RatingForm("d", null, 1L, 1L, "user"));
        ratingForms.add(new RatingForm("d", 3.3, null, 1L, "user"));
        ratingForms.add(new RatingForm("d", 3.3, 1L, null, "user"));
        ratingForms.add(new RatingForm("d", 3.3, 1L, 1L, ""));
        ratingForms.add(new RatingForm("d", 3.3, 1L, 1L, null));

        for (RatingForm form: ratingForms) {
            mvc.perform(post("/ratings")
                    .content(objectMapper.writeValueAsString(form))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user("test").roles("USER"))
            ).andExpect(status().is4xxClientError());
        }
    }

    @Test
    public void givenAlbum_whenDeleteAlbum_thenOk()
            throws Exception {
        long ratingId = 1L;
        Rating rating = getRatings("john").get(0);

        given(ratingService.delete(ratingId)).willReturn(Optional.of(rating));

        mvc.perform(delete("/ratings")
                .param("ratingId", "1")
                .with(user("test").roles("USER"))
        ).andExpect(status().isOk());
    }

    @Test
    public void givenWrongAlbum_whenDeleteAlbum_thenNotOk()
            throws Exception {

        given(ratingService.delete(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

        mvc.perform(delete("/ratings")
                .param("ratingId", "1")
                .with(user("test").roles("USER"))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoCred_whenDeleteAlbum_thenNotOk()
            throws Exception {

        given(ratingService.delete(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

        mvc.perform(delete("/ratings")
                .param("ratingId", "1")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoParam_whenDeleteAlbum_thenNotOk()
            throws Exception {

        given(ratingService.delete(ArgumentMatchers.anyLong())).willReturn(Optional.empty());

        mvc.perform(delete("/ratings")
                .with(user("test").roles("USER"))
        ).andExpect(status().is4xxClientError());
    }

    private List<Rating> getRatings(String username) {
        User user = new User(username, "sss");
        Performer performer = new Performer("bbb");
        performer.setPerformerId(2L);
        Album album1 = new Album("aa", 1992,  performer);
        Album album2 = new Album("aa", 1992,  performer);
        List<Rating> ratings = new LinkedList<>();
        ratings.add(new Rating(user, 2.0, "sss", album1));
        ratings.add(new Rating(user, 3.1, "sss", album1));
        ratings.add(new Rating(user, 4.2, "sss", album1));
        ratings.add(new Rating(user, 5.3, "sss", album2));
        ratings.add(new Rating(user, 6.4, "sss", album2));
        ratings.add(new Rating(user, 7.5, "sss", album2));
        return ratings;
    }


}
