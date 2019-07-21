package com.stankarp.ratings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.AlbumForm;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.security.jwt.JwtAuthEntryPoint;
import com.stankarp.ratings.security.jwt.JwtProvider;
import com.stankarp.ratings.security.services.UserDetailsServiceImpl;
import com.stankarp.ratings.service.AlbumService;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AlbumController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class AlbumControllerTests {

    @TestConfiguration
    static class AlbumServiceImplTestContextConfiguration {

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
    private AlbumService albumService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    public void givenAlbums_whenGetAlbums_thenReturnJsonObject()
            throws Exception {
        final String name = "test123", title = "title123";
        final int year = 1234;

        Performer performer = new Performer(name);
        performer.setPerformerId(1L);
        Album album = new Album(title, year, performer);
        album.setAlbumId(1L);

        Page<Album> albums = new PageImpl<>(Collections.singletonList(album));

        given(albumService.findAll(PageRequest.of(0, 10))).willReturn(albums);

        mvc.perform(get("/albums")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.albums", hasSize(1)))
                .andExpect(jsonPath("$._embedded.albums[0].name", is(name)))
                .andExpect(jsonPath("$._embedded.albums[0].title", is(title)))
                .andExpect(jsonPath("$._embedded.albums[0].year", is(year)));
    }

    @Test
    public void givenAlbum_whenSaveAlbum_thenReturnJsonObject()
            throws Exception {
        final String name = "test123", title = "title123";
        final int year = 1234;

        Performer performer = new Performer(name);
        performer.setPerformerId(1L);
        Album album = new Album(title, year, performer);
        album.setAlbumId(1L);

        AlbumForm albumForm = new AlbumForm(title, year, 1L, 1L);

        given(albumService.save(albumForm)).willReturn(Optional.of(album));

        mvc.perform(post("/albums")
                .content(objectMapper.writeValueAsString(albumForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER"))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.albumId", is(1)))
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.year", is(year)))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.performerId", is(1)))
                .andExpect(jsonPath("$.decade", is(123)))
                .andExpect(jsonPath("$.average", is(0.0)))
                .andExpect(jsonPath("$.ratingsCount", is(0)));
    }
}