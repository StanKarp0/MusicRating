package com.stankarp.ratings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stankarp.ratings.entity.Album;
import com.stankarp.ratings.entity.Performer;
import com.stankarp.ratings.message.request.PerformerAlbumForm;
import com.stankarp.ratings.message.request.PerformerForm;
import com.stankarp.ratings.message.request.PerformerUpdateForm;
import com.stankarp.ratings.repository.PerformerRepository;
import com.stankarp.ratings.repository.RoleRepository;
import com.stankarp.ratings.repository.UserRepository;
import com.stankarp.ratings.security.jwt.JwtAuthEntryPoint;
import com.stankarp.ratings.security.jwt.JwtProvider;
import com.stankarp.ratings.security.services.UserDetailsServiceImpl;
import com.stankarp.ratings.service.PerformerService;
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
@WebMvcTest(PerformerController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class PerformerControllerTests {

    @TestConfiguration
    static class PerformerControllerTestsContextConfiguration {

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
    private PerformerService performerService;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PerformerRepository performerRepository;

    private PerformerForm getPerformerForm(String name, String title, int year) {
        PerformerAlbumForm albumForm = new PerformerAlbumForm();
        albumForm.setTitle(title);
        albumForm.setYear(year);

        final List<PerformerAlbumForm> albums = new LinkedList<>();
        albums.add(albumForm);

        PerformerForm performerForm = new PerformerForm();
        performerForm.setName(name);
        performerForm.setAlbums(albums);

        return performerForm;
    }

    private List<Performer> getPerformers() {
        List<Performer> performers = new LinkedList<>();

        performers.add(new Performer("aa"));
        performers.add(new Performer("ab"));
        performers.add(new Performer("ac"));
        performers.add(new Performer("ad"));

        for (Performer performer: performers) {
            performer.setPerformerId(1L);
        }

        return performers;
    }

    @Test
    public void givenRatings_whenGetAll_thenOk()
            throws Exception {
        int page = 0, size = 10;
        List<Performer> performers = getPerformers();
        given(performerService.findAll(PageRequest.of(page, size))).willReturn(new PageImpl<>(performers));

        mvc.perform(get("/performers")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._embedded.performers", hasSize(performers.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void givenRatings_whenGetEmptyAll_thenNotOk()
            throws Exception {
        int page = 0, size = 10;
        given(performerService.findAll(PageRequest.of(page, size))).willReturn(new PageImpl<>(new LinkedList<>()));

        mvc.perform(get("/performers")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenQuery_whenFindQuery_thenOk()
            throws Exception {
        int page = 0, size = 10;
        List<Performer> performers = getPerformers();
        Page<Performer> performerPage = new PageImpl<>(performers);
        given(performerService.findByQuery("aaa", PageRequest.of(page, size))).willReturn(performerPage);

        mvc.perform(get("/performers/query")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("query", "aaa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenBadQuery_whenFindQuery_thenNotOk()
            throws Exception {
        mvc.perform(get("/performers/query")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenQuery_whenFindEmptyQuery_thenNotOk()
            throws Exception {
        int page = 0, size = 10;
        given(performerService.findByQuery("aaa", PageRequest.of(page, size))).willReturn(new PageImpl<>(new LinkedList<>()));

        mvc.perform(get("/performers/query")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenPerformerId_whenFindPerformer_thenOk()
            throws Exception {
        int page = 0, size = 10;
        Performer performer = getPerformers().get(0);
        performer.setPerformerId(1L);
        given(performerService.findById(1L)).willReturn(Optional.of(performer));

        mvc.perform(get("/performers/1")
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenBadPerformerId_whenFindPerformer_thenNotOk()
            throws Exception {
        mvc.perform(get("/performers/aa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNotExistingPerformerId_whenFindPerformer_thenNotOk()
            throws Exception {
        given(performerService.findById(2L)).willReturn(Optional.empty());

        mvc.perform(get("/performers/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenPerformersNoAuth_whenSave_thenIsUnauthorized() throws Exception {
        // given
        PerformerForm performerForm = getPerformerForm("ds", "dsd", 1992);

        // when save
        mvc.perform(post("/performers", performerForm)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    public void givenPerformersAuth_whenSave_thenIsOk() throws Exception {
        // given
        int year = 1993;
        String name = "test1", title = "title2";
        PerformerForm performerForm = getPerformerForm(name, title, year);
        Performer performer = new Performer(name);
        performer.addAlbum(new Album(title, year, performer));
        performer.setPerformerId(1L);

        given(performerService.save(performerForm)).willReturn(performer);

        // when save
        mvc.perform(post("/performers")
                .content(objectMapper.writeValueAsString(performerForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER"))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.performerId", is(1)))
                .andExpect(jsonPath("$.albumCount", is(1)))
                .andExpect(jsonPath("$.ratingsCount", is(0)))
                .andExpect(jsonPath("$.average", is(0.0)));
    }

    @Test
    public void givenPerformersAuth_whenUpdate_thenIsOk() throws Exception {
        // given
        long id = 1L;
        String name = "test1";
        PerformerUpdateForm performerForm = new PerformerUpdateForm(id, name);
        Performer performer = new Performer(name);
        performer.setPerformerId(id);

        given(performerService.update(performerForm)).willReturn(performer);

        // when save
        mvc.perform(put("/performers")
                .content(objectMapper.writeValueAsString(performerForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER"))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.performerId", is(1)))
                .andExpect(jsonPath("$.albumCount", is(0)))
                .andExpect(jsonPath("$.ratingsCount", is(0)))
                .andExpect(jsonPath("$.average", is(0.0)));
    }

    @Test
    public void givenBadForm_whenUpdate_thenIsNotOk() throws Exception {
        // given
        long id = 1L;
        String name = "test1";
        PerformerUpdateForm performerForm = new PerformerUpdateForm();
        Performer performer = new Performer(name);
        performer.setPerformerId(id);

        given(performerService.update(performerForm)).willReturn(performer);

        // when save
        mvc.perform(put("/performers")
                .content(objectMapper.writeValueAsString(performerForm))
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("test").roles("USER"))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenPerformer_whenDeletePerformer_thenOk()
            throws Exception {
        final String name = "test123";

        Performer performer = new Performer(name);
        performer.setPerformerId(1L);

        given(performerService.delete(1L)).willReturn(Optional.of(performer));

        mvc.perform(delete("/performers")
                .param("performerId", "1")
                .with(user("test").roles("USER"))
        ).andExpect(status().isOk());
    }


    @Test
    public void givenWrongPerformer_whenDeletePerformer_thenNotOk()
            throws Exception {
        mvc.perform(delete("/performers")
                .param("performerId", "1")
                .with(user("test").roles("USER"))
        ).andExpect(status().is4xxClientError());
    }


    @Test
    public void givenPerformer_whenDeletePerformerNoAuth_thenNotOk()
            throws Exception {
        mvc.perform(delete("/performers")
                .param("performerId", "aa")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenWrongPerformerId_whenDeletePerformer_thenNotOk()
            throws Exception {
        mvc.perform(delete("/performers")
                .param("performerId", "aa")
                .with(user("test").roles("USER"))
        ).andExpect(status().is4xxClientError());
    }

}
