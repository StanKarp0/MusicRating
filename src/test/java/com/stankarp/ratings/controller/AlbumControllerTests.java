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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void givenEmptyAlbums_whenFindAll_thenEmpty()
            throws Exception {

        Page<Album> albums = new PageImpl<>(Collections.emptyList());

        given(albumService.findAll(PageRequest.of(0, 10))).willReturn(albums);

        mvc.perform(get("/albums")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAlbumId_whenFindAlbum_thenOk()
            throws Exception {
        Performer performer = new Performer("bbb");
        Album album = new Album("ss", 1992, performer);
        album.setAlbumId(1L);
        given(albumService.findById(1L)).willReturn(Optional.of(album));

        mvc.perform(get("/albums/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenBadAlbumId_whenFindAlbum_thenNotOk()
            throws Exception {
        mvc.perform(get("/albums/aa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNotExistingAlbumId_whenFindAlbum_thenNotOk()
            throws Exception {
        given(albumService.findById(2L)).willReturn(Optional.empty());

        mvc.perform(get("/albums/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenQuery_whenFindQuery_thenOk()
            throws Exception {
        int page = 0, size = 10;
        List<Album> albums = new LinkedList<>();
        Performer performer = new Performer("bbb");
        albums.add(new Album("ss", 1992, performer));
        Page<Album> albumsPage = new PageImpl<>(albums);
        given(albumService.findByQuery("aaa", PageRequest.of(page, size))).willReturn(albumsPage);

        mvc.perform(get("/albums/query")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("query", "aaa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenBadQuery_whenFindQuery_thenNotOk()
            throws Exception {
        mvc.perform(get("/albums/query")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenEmptyQuery_whenFindQuery_thenOk()
            throws Exception {
        int page = 0, size = 10;
        given(albumService.findByQuery("aaa", PageRequest.of(page, size)))
                .willReturn(new PageImpl<>(new LinkedList<>()));

        mvc.perform(get("/albums/query")
                .param("query", "aaa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoQuery_whenFindQuery_thenNotOk()
            throws Exception {

        mvc.perform(get("/albums/query")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenPerformerId_whenFindPerformer_thenOk()
            throws Exception {
        int page = 0, size = 10;
        List<Album> albums = new LinkedList<>();
        Performer performer = new Performer("bbb");
        albums.add(new Album("ss", 1992, performer));
        Page<Album> albumsPage = new PageImpl<>(albums);
        given(albumService.findByPerformerId(1L, PageRequest.of(page, size))).willReturn(albumsPage);

        mvc.perform(get("/albums/performer")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("performerId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenBadPerformerId_whenFindPerformer_thenNotOk()
            throws Exception {
        mvc.perform(get("/albums/performer")
                .param("performerId", "aa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNotExistingPerformerId_whenFindPerformer_thenNotOk()
            throws Exception {
        mvc.perform(get("/albums/performer")
                .param("performerId", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenPerformerId_whenFindEmptyPerformer_thenNotOk()
            throws Exception {
        int page = 0, size = 10;
        given(albumService.findByPerformerId(1L, PageRequest.of(page, size)))
                .willReturn(new PageImpl<>(new LinkedList<>()));

        mvc.perform(get("/albums/performer")
                .param("performerId", "1")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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

    @Test
    public void givenWrongAlbum_whenSaveAlbum_thenNotOk()
            throws Exception {
        AlbumForm albumForm = new AlbumForm(null, 1992, null, 1L);

        given(albumService.save(albumForm)).willReturn(Optional.empty());

        mvc.perform(post("/albums")
                .content(objectMapper.writeValueAsString(albumForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER"))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenAlbum_whenDeleteAlbum_thenOk()
            throws Exception {
        final String name = "test123", title = "title123";
        final int year = 1234;

        Performer performer = new Performer(name);
        performer.setPerformerId(1L);
        Album album = new Album(title, year, performer);
        album.setAlbumId(1L);

        given(albumService.delete(1L)).willReturn(Optional.of(album));

        mvc.perform(delete("/albums")
                .param("albumId", "1")
                .with(user("test").roles("USER"))
        ).andExpect(status().isOk());
    }

    @Test
    public void givenWrongAlbum_whenDeleteAlbum_thenNotOk()
            throws Exception {
        given(albumService.delete(1L)).willReturn(Optional.empty());

        mvc.perform(delete("/albums")
                .param("albumId", "1")
                .with(user("test").roles("USER"))
        ).andExpect(status().is4xxClientError());
    }
}